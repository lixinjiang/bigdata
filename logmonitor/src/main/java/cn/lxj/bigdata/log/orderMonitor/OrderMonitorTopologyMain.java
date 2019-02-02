package cn.lxj.bigdata.log.orderMonitor;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.lxj.bigdata.log.orderMonitor.bolt.PaymentInfoParserBolt;
import cn.lxj.bigdata.log.orderMonitor.bolt.SaveInfo2DB;
import cn.lxj.bigdata.log.orderMonitor.spout.RandomSpout;

/**
 * OrderMonitorTopologyMain
 * description 对用户的订单进行分析，主要分析是否有欺诈行为
 * 比如：
 * ip地址列表分析
 * 用户登录设备列表分析
 * 用户平均下单时长分析、下单时间分析、喜好品类、客单价等信息
 * 用户收货地址列表信息
 * 用户收获手机号码列表信息
 * 商品属性是否有交易变现、高价值等特点
 * 支付是否选择货到付款、信用卡支付
 * <p>数据准备：</p>
 * 订单基本信息，包括顶点编号、订单价格、商品列表，以双十一消息为主
 * <p>基础数据：</p>
 * 基础数据存放在redis中，需要设计相关的数据结构
 * 判断条件（触发规则）：
 * 规则1：商品属性：属于易变现，并且具有高价值的特点
 * 用户属性：收获地址不在常用的收获地址中
 * 订单属性：订单支付金额在2000以上
 * 规则2：商品属性
 * 用户属性：不在常用IP地址内，近期修改过密码
 * 订单属性：
 * <p>
 * 判断基本流程：
 * 1.订单mq进来之后，对mq进行解析并校验所有基础属性，生成一个规则结果数据
 * 2.对结果数据进行判断，生成触发规则信息
 * 3.将触发规则信息回写到数据库
 * create class by lxj 2019/2/1
 **/
public class OrderMonitorTopologyMain {
    public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("kafka-spout", new RandomSpout(), 2);
        topologyBuilder.setBolt("paymentInfoParser-bolt", new PaymentInfoParserBolt(), 3);
        topologyBuilder.setBolt("SaveInfo2DB-bolt", new SaveInfo2DB(), 2)
                .fieldsGrouping("paymentInfoParser-bolt", new Fields("orderId"))
                .fieldsGrouping("kafka-spout", new Fields("paymentInfo"));
        // 启动topology的配置信息
        Config topologConf = new Config();
        // TOPOLOGY_DEBUG(setDebug), 当它被设置成true的话， storm会记录下每个组件所发射的每条消息。
        // 这在本地环境调试topology很有用， 但是在线上这么做的话会影响性能的。
//        topologConf.setDebug(true);
        // storm的运行有两种模式: 本地模式和分布式模式.
        if (args != null && args.length > 0) {
            // 定义你希望集群分配多少个工作进程给你来执行这个topology
            topologConf.setNumWorkers(2);
            // 向集群提交topology
            StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, topologyBuilder.createTopology());
        } else {
            topologConf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("order-monitor", topologConf, topologyBuilder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
    }
}