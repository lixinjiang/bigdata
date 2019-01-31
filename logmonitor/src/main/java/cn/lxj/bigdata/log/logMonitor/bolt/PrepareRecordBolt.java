package cn.lxj.bigdata.log.logMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import cn.lxj.bigdata.log.logMonitor.domain.Message;
import cn.lxj.bigdata.log.logMonitor.domain.Record;
import cn.lxj.bigdata.log.logMonitor.utils.MonitorHandler;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * PrepareRecordBolt
 * description  将触发信息保存到mysql数据库中
 * 手动调用ack方法，BaseBasicBolt由storm框架自动调用ack方法
 * create class by lxj 2019/1/31
 **/
public class PrepareRecordBolt extends BaseBasicBolt{
    private static Logger logger = LoggerFactory.getLogger(PrepareRecordBolt.class);

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Message message = (Message) input.getValueByField("message");
        String appId = message.getAppId();
        // 将触发的规则信息进行通知
        MonitorHandler.notifly(appId, message);
        Record record = new Record();
        try {
            BeanUtils.copyProperties(record,message);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("record"));
    }
}
