package cn.lxj.bigdata.log.logMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.lxj.bigdata.log.logMonitor.domain.Message;
import cn.lxj.bigdata.log.logMonitor.utils.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FilterBolt
 * description
 * create class by lxj 2019/1/31
 **/
public class FilterBolt extends BaseBasicBolt {
    private static Logger logger = LoggerFactory.getLogger(FilterBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        // 获取KafkaSpout发送出来的数据
        String line = input.getString(0);
        // byte[] value = (byte[]) input.getValue(0);
        // 将数组转化为字符串
        // String line = new String(value);
        //对数据进行解析
        // appid   content
        //1  error: Caused by: java.lang.NoClassDefFoundError: com/starit/gejie/dao/SysNameDao
        Message message = MonitorHandler.parser(line);
        if (message == null) {
            return;
        }
        if (MonitorHandler.trigger(message)) {
            collector.emit(new Values(message.getAppId(), message));
        }
        //定时更新规则信息
        MonitorHandler.scheduleLoad();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("appId", "message"));
    }
}
