package cn.lxj.bigdata;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * MySpiltBolt
 * description
 * create class by lxj 2019/1/18
 **/
public class MySpiltBolt extends BaseRichBolt{
    OutputCollector collector;
    /**
     * 初始化方法
     * @param stormConf
     * @param context
     * @param collector
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 被storm框架 while（true）循环调用，传入参数tuple
     * @param input
     */
    @Override
    public void execute(Tuple input) {
        String line = input.getString(0);
        String[] arrWords = line.split(" ");
        for (String word : arrWords) {
            collector.emit(new Values(word, 1));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word","num"));
    }
}