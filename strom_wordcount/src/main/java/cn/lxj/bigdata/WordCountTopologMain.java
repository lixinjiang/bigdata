package cn.lxj.bigdata;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * WordCountTopologMain
 * description
 * create class by lxj 2019/1/18
 **/
public class WordCountTopologMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        //1、准备一个TopologyBuilder
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MySpout(), 2);
        topologyBuilder.setBolt("mybolt1", new MySpiltBolt(), 2).shuffleGrouping("mySpout");
        // 按照word分发到指定机器上执行
        topologyBuilder.setBolt("mybolt2", new MyCountBolt(), 2).fieldsGrouping("mybolt1", new Fields("word"));
        /**
         * i
         * am
         * lilei
         * love
         * hanmeimei
         */
        //2、创建一个Configuration，用来指定当前topology需要的worker数量
        Config config = new Config();
        config.setNumWorkers(2);

        //3、提交任务----两种模式，本地模式和集群模式
//        StormSubmitter.submitTopology("mywordcount", config, topologyBuilder.createTopology());
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("mywordcount", config, topologyBuilder.createTopology());
    }
}