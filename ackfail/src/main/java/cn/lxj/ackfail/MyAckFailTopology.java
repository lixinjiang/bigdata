package cn.lxj.ackfail;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

/**
 * MyAckFailTopology
 * description
 * create class by lxj 2019/1/28
 **/
public class MyAckFailTopology {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MySpout(), 1);
        topologyBuilder.setBolt("myBolt", new MyBolt(), 1).shuffleGrouping("mySpout");

        Config config = new Config();
        String name = MyAckFailTopology.class.getSimpleName();
        if (args != null && args.length > 0) {
            String nimbus = args[0];
            config.put(Config.NIMBUS_HOST, nimbus);
            config.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(name, config, topologyBuilder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, config, topologyBuilder.createTopology());
            Thread.sleep(60 * 60 * 1000);
            cluster.shutdown();
        }
    }
}