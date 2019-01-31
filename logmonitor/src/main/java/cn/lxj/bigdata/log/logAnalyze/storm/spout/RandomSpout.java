package cn.lxj.bigdata.log.logAnalyze.storm.spout;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import cn.lxj.bigdata.log.logAnalyze.storm.domain.LogMessage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * RandomSpout
 * description 随机产生消息出去
 * create class by lxj 2019/1/31
 **/
public class RandomSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private List<LogMessage> list;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;
        list = new ArrayList<>();
        list.add(new LogMessage(1, "http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/", "maoxiangyi"));
        list.add(new LogMessage(1, "http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/", "maoxiangyi"));
        list.add(new LogMessage(1, "http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/", "maoxiangyi"));
        list.add(new LogMessage(1, "http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/", "maoxiangyi"));
    }

    @Override
    public void nextTuple() {
        final Random rand = new Random();
        LogMessage msg = list.get(rand.nextInt(4));
        this.collector.emit(new Values(new Gson().toJson(msg)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("paymentInfo"));
    }
}
