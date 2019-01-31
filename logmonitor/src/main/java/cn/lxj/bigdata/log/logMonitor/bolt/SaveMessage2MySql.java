package cn.lxj.bigdata.log.logMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.lxj.bigdata.log.logMonitor.domain.Record;
import cn.lxj.bigdata.log.logMonitor.utils.MonitorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SaveMessage2MySql
 * description
 * create class by lxj 2019/1/31
 **/
public class SaveMessage2MySql extends BaseBasicBolt {
    private static Logger logger = LoggerFactory.getLogger(SaveMessage2MySql.class);
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        Record record = (Record) input.getValueByField("record");
        MonitorHandler.save(record);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}