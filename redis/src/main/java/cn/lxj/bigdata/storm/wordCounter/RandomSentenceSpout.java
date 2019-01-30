package cn.lxj.bigdata.storm.wordCounter;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;
import java.util.Random;

/**
 * RandomSentenceSpout
 * description
 * create class by lxj 2019/1/30
 **/
public class RandomSentenceSpout extends BaseRichSpout {
    private static final long serialVersionUID = 5028304756439810609L;
    //用来收集Spout输出的tuple
    SpoutOutputCollector collector;
    Random rand;

    //该方法调用一次，主要由storm框架传入SpoutOutputCollector
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        rand = new Random();
    }

    //该方法会被循环调用
    public void nextTuple() {
        String[] sentences = new String[]{"the cow jumped over the moon",
                "an apple a day keeps the doctor away",
                "four score and seven years ago",
                "snow white and the seven dwarfs", "i am at two with nature"};
        String sentence = sentences[rand.nextInt(sentences.length)];
        //public class Values extends ArrayList<Object>
        collector.emit(new Values(sentence));
    }

    // 消息源可以发射多条消息流stream。多条消息流可以理解为多种类型的数据。
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("sentence"));
    }
}