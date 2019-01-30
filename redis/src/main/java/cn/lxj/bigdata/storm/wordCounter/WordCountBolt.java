package cn.lxj.bigdata.storm.wordCounter;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * WordCountBolt
 * description
 * create class by lxj 2019/1/30
 **/
public class WordCountBolt extends BaseBasicBolt {
    private static final long serialVersionUID = 5678586644899822142L;
    Map<String, Integer> counters = new HashMap<String, Integer>();

    @Override
    public void prepare(Map stormConf, TopologyContext context) {
        super.prepare(stormConf, context);
    }

    /*
     * 将collector中的元素存放在成员变量counters（Map）中.
     * 如果counters（Map）中已经存在该元素，getValule并对Value进行累加操作。
     */
    public void execute(Tuple input, BasicOutputCollector collector) {
        String str = input.getString(0);
        input.getFields();
        if (!counters.containsKey(str)) {
            counters.put(str, 1);
        } else {
            Integer c = counters.get(str) + 1;
            counters.put(str, c);
        }
        System.out.println(counters);
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}