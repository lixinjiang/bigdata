package cn.lxj.bigdata.log.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogMessage;
import cn.lxj.bigdata.log.logAnalyze.storm.utils.LogAnalyzeHandler;

/**
 * MessageFilterBolt
 * description
 * create class by lxj 2019/1/31
 **/
public class MessageFilterBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        // 获取Kafka发送出来的消息
        String line = input.getString(0);
        // 对数据进行解析
        LogMessage logMessage = LogAnalyzeHandler.parser(line);
        if (logMessage != null && LogAnalyzeHandler.isValidType(logMessage.getType())) {
            collector.emit(new Values(logMessage.getType(), logMessage));
            // 定时更新规则信息
            LogAnalyzeHandler.scheduleLoad();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //根据点击内容类型将日志进行区分
        declarer.declare(new Fields("type", "message"));
    }
}
