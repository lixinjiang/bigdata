package cn.lxj.bigdata.log.logAnalyze.app.callback;

import cn.lxj.bigdata.log.logAnalyze.app.domain.BaseRecord;
import cn.lxj.bigdata.log.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.lxj.bigdata.log.logAnalyze.storm.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

/**
 * HalfAppendCallBack
 * description 计算每半个小时的数据
 * create class by lxj 2019/1/30
 **/
public class HalfAppendCallBack implements Runnable{
    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        if (calendar.get(Calendar.MINUTE) % 30 == 0) {
            String endTime = DateUtils.getDataTime(calendar);
            String startTime = DateUtils.before30Minute(calendar);
            LogAnalyzeDao logAnalyzeDao = new LogAnalyzeDao();
            List<BaseRecord> baseRecordList = logAnalyzeDao.sumRecordValue(startTime, endTime);
            logAnalyzeDao.saveHalfAppendRecord(baseRecordList);
        }
    }
}