package cn.lxj.ackfail;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * MyBolt
 * description
 * create class by lxj 2019/1/28
 **/
public class MyBolt extends BaseRichBolt {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBolt.class);
    private OutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        LOGGER.info("=====================MyBolt.declareOutputFields=====================");
        outputFieldsDeclarer.declare(new Fields("word"));
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        LOGGER.info("=====================MyBolt.prepare=====================");
        this.collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        LOGGER.info("=====================MyBolt.execute=====================");
        String sentence = tuple.getString(0);
        String[] words = sentence.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!word.isEmpty()) {
                word = word.toLowerCase();
                collector.emit(tuple, new Values(word));
            }
        }
        collector.ack(tuple);
    }
}