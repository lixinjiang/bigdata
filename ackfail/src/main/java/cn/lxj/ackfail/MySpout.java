package cn.lxj.ackfail;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * MySpout
 * description  ack/fail机制
 * create class by lxj 2019/1/28
 **/
public class MySpout extends BaseRichSpout {
    private static final Logger logger = LoggerFactory.getLogger(MySpout.class);
    private SpoutOutputCollector spoutOutputCollector;
    private Random random;
    private Map<String, Values> buffer = new HashMap<>();

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        logger.info("=====================MySpout.declareOutputFields=====================");
        outputFieldsDeclarer.declare(new Fields("sentence"));
        random = new Random();
    }

    @Override

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        logger.info("=====================MySpout.open=====================");
        this.spoutOutputCollector = spoutOutputCollector;
    }

    @Override
    public void nextTuple() {
        logger.info("=====================MySpout.nextTuple=====================");
        String[] sentences = new String[]{
                "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon",
                "the cow jumped over the moon"
        };
        String sentence = sentences[random.nextInt(sentences.length)];
        String messageId = UUID.randomUUID().toString().replace("-", "");
        Values tuple = new Values(sentence);
        spoutOutputCollector.emit(tuple, messageId);
        buffer.put(messageId, tuple);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ack(Object msgId) {
        logger.info("消息处理成功，id=" + msgId);
        buffer.remove(msgId);
    }

    @Override
    public void fail(Object msgId) {
        logger.info("消息处理失败，id=" + msgId);
        Values tuple = buffer.get(msgId);
        spoutOutputCollector.emit(tuple, msgId);
    }
}