package cn.lxj.bigdata.storm.wordCounter;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * SplitSentenceBolt
 * description
 * create class by lxj 2019/1/30
 **/
public class SplitSentenceBolt extends BaseBasicBolt {
    private static final long serialVersionUID = -5283595260540124273L;

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    public void execute(Tuple input, BasicOutputCollector collector) {
        String sentence = input.getString(0);
        String[] words = sentence.split(" ");
        for (String word : words) {
            word = word.trim();
            if (!"".equals(word)) {
                word = word.toLowerCase();
                collector.emit(new Values(word));
            }
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word"));
    }
}