package cn.lxj.bigdata.log.logAnalyze.app;

import cn.lxj.bigdata.log.logAnalyze.app.callback.DayAppendCallBack;
import cn.lxj.bigdata.log.logAnalyze.app.callback.HalfAppendCallBack;
import cn.lxj.bigdata.log.logAnalyze.app.callback.HourAppendCallBack;
import cn.lxj.bigdata.log.logAnalyze.app.callback.OneMinuteCallBack;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * ReportDataWorker
 * description
 *  计算每个指标每分钟的增量数据
 * create class by lxj 2019/1/30
 **/
public class ReportDataWorker {
    public static void main(String[] args) {
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);
//        计算每分钟的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new OneMinuteCallBack(), 0, 60, TimeUnit.SECONDS);
//        计算每半个小时的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new HalfAppendCallBack(), 0, 60, TimeUnit.SECONDS);
//        计算每小时的增量数据，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new HourAppendCallBack(), 0, 60, TimeUnit.SECONDS);
//        计算每天的全量，并将增量数据保存到mysql数据库中
        scheduledExecutorService.scheduleAtFixedRate(new DayAppendCallBack(), 0, 60, TimeUnit.SECONDS);
    }
}