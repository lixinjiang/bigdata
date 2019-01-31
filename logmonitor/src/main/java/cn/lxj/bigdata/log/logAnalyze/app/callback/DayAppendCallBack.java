package cn.lxj.bigdata.log.logAnalyze.app.callback;


import cn.lxj.bigdata.log.logAnalyze.app.domain.BaseRecord;
import cn.lxj.bigdata.log.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.lxj.bigdata.log.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * DayAppendCallBack
 * description 计算每天的全量数据
 * create class by lxj 2019/1/30
 **/
public class DayAppendCallBack implements Runnable{

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.HOUR) == 0) {//凌晨
            String startTime = DateUtils.beforeOneDay(calendar);
            String endTime = DateUtils.getDataTime(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveDayAppendRecord(baseRecordList);
        }
    }
}