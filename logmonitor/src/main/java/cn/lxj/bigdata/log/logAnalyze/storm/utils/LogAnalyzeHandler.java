package cn.lxj.bigdata.log.logAnalyze.storm.utils;

import cn.lxj.bigdata.log.logAnalyze.storm.constant.LogTypeConstant;
import cn.lxj.bigdata.log.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogAnalyzeJob;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogAnalyzeJobDetail;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogMessage;
import com.google.gson.Gson;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LogAnalyzeHandler
 * description
 * create class by lxj 2019/1/31
 **/
public class LogAnalyzeHandler {
    // 定时加载配置文件标识
    private static boolean reloaded = false;
    // 用来保存job信息，key为jobType，value为该类别下所有的任务
    private static Map<String, List<LogAnalyzeJob>> jobMap;
    // 用来保存job的判断条件，key为jobId，value为list，list中封装了很多判断条件
    private static Map<String, List<LogAnalyzeJobDetail>> jobDetail;

    static {
        jobMap = loadJobMap();
        jobDetail = loadJobDetailMap();
    }

    public static LogMessage parser(String line) {
        return new Gson().fromJson(line, LogMessage.class);
    }

    public static void process(LogMessage logMessage) {
        if (jobMap == null || jobDetail == null) {
            loadDataModel();
        }

        // kafka来的日志：2,req,ref,xxx,xxx,xxx,yy

        List<LogAnalyzeJob> analyzeJobList = jobMap.get(logMessage.getType() + "");
        for (LogAnalyzeJob logAnalyzeJob : analyzeJobList) {
            boolean isMatch = false;// 是否匹配
            List<LogAnalyzeJobDetail> logAnalyzeJobDetails = jobDetail.get(logAnalyzeJob.getJobId());
            for (LogAnalyzeJobDetail jobDetail : logAnalyzeJobDetails) {
                // jobDetail,指定和kafka输入过来的数据中的requesturl比较
                // 获取kafka输入过来的数据的requesturl的值
                String fieldValueInLog = logMessage.getCompareFieldValue(jobDetail.getField());
                // 1、包含，2：等于，3：正则
                if (jobDetail.getCompare() == 1 && fieldValueInLog.contains(jobDetail.getValue())) {
                    isMatch = true;
                } else if (jobDetail.getCompare() == 2 && fieldValueInLog.equals(jobDetail.getValue())) {
                    isMatch = true;
                }
                if (!isMatch) {
                    break;
                }
            }
            if (isMatch) {
                // 设置py
                String pvKey = "log:" + logAnalyzeJob.getJobName() + ":py:" + DateUtils.getDate();
                String uvKey = "log:" + logAnalyzeJob.getJobName() + ":uv:" + DateUtils.getDate();
                ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
                // 给pv+1
                jedis.incr(pvKey);
                // 设置uv，uv需要去重，使用set
                jedis.sadd(uvKey, logMessage.getUserName());
            }
        }
    }

    private static void processViewLog(LogMessage logMessage) {
        if (jobMap == null || jobDetail == null) {
            loadDataModel();
        }
        List<LogAnalyzeJob> analyzeJobList = jobMap.get(LogTypeConstant.VIEW + "");
        for (LogAnalyzeJob logAnalyzeJob : analyzeJobList) {
            boolean isMatch = false;
            List<LogAnalyzeJobDetail> logAnalyzeJobDetailList = jobDetail.get(logAnalyzeJob.getJobId());
            for (LogAnalyzeJobDetail jobDetail : logAnalyzeJobDetailList) {
                String fieldValueInLog = logMessage.getCompareFieldValue(jobDetail.getField());
                //1:包含 2:等于
                if (jobDetail.getCompare() == 1 && fieldValueInLog.contains(jobDetail.getValue())) {
                    isMatch = true;
                } else if (jobDetail.getCompare() == 2 && fieldValueInLog.equals(jobDetail.getValue())) {
                    isMatch = true;
                } else {
                    isMatch = false;
                }
                if (!isMatch) {
                    break;
                }
            }
            if (isMatch) {
                //设置pv
                String pvKey = "log:" + logAnalyzeJob.getJobName() + ":pv:" + DateUtils.getDate();
                String uvKey = "log:" + logAnalyzeJob.getJobName() + ":uv:" + DateUtils.getDate();
                ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
                jedis.incr(pvKey);
                //设置uv
                jedis.sadd(uvKey, logMessage.getUserName());
                //优惠策略，使用bloomFilter算法进行优化
            }
        }
    }

    private synchronized static void loadDataModel() {
        if (jobMap == null) {
            jobMap = loadJobMap();
        }
        if (jobDetail == null) {
            jobDetail = loadJobDetailMap();
        }
    }

    private static Map<String, List<LogAnalyzeJobDetail>> loadJobDetailMap() {
        Map<String, List<LogAnalyzeJobDetail>> map = new HashMap<String, List<LogAnalyzeJobDetail>>();
        List<LogAnalyzeJobDetail> logAnalyzeJobDetailList = new LogAnalyzeDao().loadJobDetailList();
        for (LogAnalyzeJobDetail logAnalyzeJobDetail : logAnalyzeJobDetailList) {
            int jobId = logAnalyzeJobDetail.getJobId();
            List<LogAnalyzeJobDetail> jobDetails = map.get(jobId + "");
            if (jobDetails == null || jobDetails.size() == 0) {
                jobDetails = new ArrayList<>();
                map.put(jobId + "", jobDetails);
            }
            jobDetails.add(logAnalyzeJobDetail);
        }
        System.out.println("jobDetailMap:  "+map);
        return map;
    }

    private static Map<String, List<LogAnalyzeJob>> loadJobMap() {
        Map<String, List<LogAnalyzeJob>> map = new HashMap<>();
        List<LogAnalyzeJob> logAnalyzeJobList = new LogAnalyzeDao().loadJobList();
        System.out.println(logAnalyzeJobList);
        for (LogAnalyzeJob logAnalyzeJob : logAnalyzeJobList) {
            int jobType = logAnalyzeJob.getJobType();
            if (isValidType(jobType)) {
                List<LogAnalyzeJob> jobList = map.get(jobType+"");
                if (jobList == null || jobList.size() == 0) {
                    jobList = new ArrayList<>();
                    map.put(jobType + "", jobList);
                }
                jobList.add(logAnalyzeJob);
            }
        }
        System.out.println("job:  " + map);
        return map;
    }

    public static boolean isValidType(int jobType) {
        return jobType == LogTypeConstant.BUY || jobType == LogTypeConstant.CLICK
                || jobType == LogTypeConstant.VIEW || jobType == LogTypeConstant.SEARCH;
    }

    /**
     * 配置scheduleLoad重新加载底层数据模型。
     */
    public static synchronized void reloadDataModel() {
        if (reloaded) {
            jobMap = loadJobMap();
            jobDetail = loadJobDetailMap();
            reloaded = false;
        }
    }

    /**
     * 定时加载配置信息
     * 配合reloadDataModel模块一起使用。
     * 主要实现原理如下：
     * 1，获取分钟的数据值，当分钟数据是10的倍数，就会触发reloadDataModel方法，简称reload时间。
     * 2，reloadDataModel方式是线程安全的，在当前worker中只有一个线程能够操作。
     * 3，为了保证当前线程操作完毕之后，其他线程不再重复操作，设置了一个标识符reloaded。
     * 在非reload时间段时，reloaded一直被置为true；
     * 在reload时间段时，第一个线程进入reloadDataModel后，加载完毕之后会将reloaded置为false。
     */
    public static void scheduleLoad() {
        String date = DateUtils.getDateTime();
        int now = Integer.parseInt(date.split(":")[1]);
        if (now % 10 == 0) {//每10分钟加载一次
            reloadDataModel();
        } else {
            reloaded = true;
        }
    }
}