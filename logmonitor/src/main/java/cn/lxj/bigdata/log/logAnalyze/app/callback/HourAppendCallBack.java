package cn.lxj.bigdata.log.logAnalyze.app.callback;

import cn.lxj.bigdata.log.logAnalyze.app.domain.BaseRecord;
import cn.lxj.bigdata.log.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.lxj.bigdata.log.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * HourAppendCallBack
 * description 计算每小时的增量数据
 * create class by lxj 2019/1/30
 **/
public class HourAppendCallBack implements Runnable{

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        System.out.println(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.MINUTE) == 59) {
            //获取所有的增量数据
            String endTime = DateUtils.getDataTime(calendar);
            String startTime = DateUtils.beforeOneHour(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHourAppendRecord(baseRecordList);
        }
    }
}