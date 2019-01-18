package cn.lxj.bigdata;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.Map;

/**
 * MySpout
 * description
 * create class by lxj 2019/1/18
 **/
public class MySpout extends BaseRichSpout {
    SpoutOutputCollector collector;

    /**
     * 初始化方法
     *
     * @param conf
     * @param context
     * @param collector
     */
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
    }

    /**
     * Storm框架在 while（true） 调用nextTuple
     */
    @Override
    public void nextTuple() {
        collector.emit(new Values("i am lilei love hanmeimei"));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("love"));
    }
}