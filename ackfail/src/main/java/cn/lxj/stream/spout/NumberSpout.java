package cn.lxj.stream.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * NumberSpout
 * description
 * create class by lxj 2019/1/28
 **/
public class NumberSpout extends BaseRichSpout {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberSpout.class);
    SpoutOutputCollector spoutOutputCollector;
    Random random;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        this.spoutOutputCollector = spoutOutputCollector;
        this.random = new Random();
    }

    @Override
    public void nextTuple() {
        String[] sentences = new String[]{
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666",
                "111 222 333 4444 555 666"
        };
        String sentence = sentences[random.nextInt(sentences.length)];
        String messageId = UUID.randomUUID().toString().replace("-", "");
        spoutOutputCollector.emit(new Values(sentence), messageId);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(new Fields("content"));
    }
}