package cn.lxj.bigdata.log.logAnalyze.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.lxj.bigdata.log.logAnalyze.storm.bolt.MessageFilterBolt;
import cn.lxj.bigdata.log.logAnalyze.storm.bolt.ProcessMessage;
import cn.lxj.bigdata.log.logAnalyze.storm.spout.RandomSpout;

/**
 * LogAnalyzeTopologyMain
 * description
 * create class by lxj 2019/1/30
 **/
public class LogAnalyzeTopologyMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafka-spout", new RandomSpout(), 2);
        topologyBuilder.setBolt("MessageFilter-bolt", new MessageFilterBolt(), 3).shuffleGrouping("kafka-spout");
        topologyBuilder.setBolt("ProcessMessage-bolt", new ProcessMessage(), 3).fieldsGrouping("MessageFilter-bolt",
                new Fields("type"));
        Config topologConf = new Config();
        if (args != null && args.length > 0) {
            topologConf.setNumWorkers(2);
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, topologyBuilder.createTopology());
        } else {
            topologConf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("LogAnalyzeTopologyMain", topologConf, topologyBuilder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}