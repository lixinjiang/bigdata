package cn.lxj.bigdata.log.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogMessage;
import cn.lxj.bigdata.log.logAnalyze.storm.utils.LogAnalyzeHandler;

/**
 * ProcessMessage
 * description
 * create class by lxj 2019/1/31
 **/
public class ProcessMessage extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        LogMessage logMessage = (LogMessage) input.getValueByField("message");
        LogAnalyzeHandler.process(logMessage);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //
    }
}
