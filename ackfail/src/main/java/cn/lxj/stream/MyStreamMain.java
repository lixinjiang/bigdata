package cn.lxj.stream;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import cn.lxj.stream.bolt.SaveDataBolt;
import cn.lxj.stream.bolt.SplitBolt;
import cn.lxj.stream.spout.NumberSpout;
import cn.lxj.stream.spout.SignSpout;
import cn.lxj.stream.spout.StringSpout;
import cn.lxj.stream.streamBolt.NumberStreamBolt;
import cn.lxj.stream.streamBolt.SignStreamBolt;
import cn.lxj.stream.streamBolt.StringStreamBolt;

/**
 * MyStreamMain
 * description
 * create class by lxj 2019/1/28
 **/
public class MyStreamMain {
    public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        // set spouts 分
        topologyBuilder.setSpout("NumberSpout", new NumberSpout(), 1);
        topologyBuilder.setSpout("StringSpout", new StringSpout(), 1);
        topologyBuilder.setSpout("SignSpout", new SignSpout(), 1);

        //set bolts 合
        topologyBuilder.setBolt("SplitBolt", new SplitBolt(), 1)
                .shuffleGrouping("NumberSpout")
                .shuffleGrouping("StringSpout")
                .shuffleGrouping("SignSpout");

        //set bolts 分
        topologyBuilder.setBolt("StringStreamBolt", new StringStreamBolt(), 1).shuffleGrouping("SplitBolt", "string-stream");
        topologyBuilder.setBolt("NumberStreamBolt", new NumberStreamBolt(), 1).shuffleGrouping("SplitBolt", "number-stream");
        topologyBuilder.setBolt("SignStreamBolt", new SignStreamBolt(), 1).shuffleGrouping("SplitBolt", "sign-stream");

        //set bolt 合
        topologyBuilder.setBolt("SaveDataBolt", new SaveDataBolt(), 3)
                .fieldsGrouping("StringStreamBolt", new Fields("type"))
                .fieldsGrouping("NumberStreamBolt", new Fields("type"))
                .fieldsGrouping("SignStreamBolt", new Fields("type"));

        Config conf = new Config();
        String name = MyStreamMain.class.getSimpleName();
        if (args != null && args.length > 0) {
            String nimbus = args[0];
            conf.put(Config.NIMBUS_HOST, nimbus);
            conf.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(name, conf, topologyBuilder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, topologyBuilder.createTopology());
            Thread.sleep(60 * 60 * 1000);
            cluster.shutdown();
        }
    }
}