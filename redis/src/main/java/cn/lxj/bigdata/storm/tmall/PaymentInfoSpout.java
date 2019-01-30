package cn.lxj.bigdata.storm.tmall;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * PaymentInfoSpout
 * description
 * create class by lxj 2019/1/30
 **/
public class PaymentInfoSpout extends BaseRichSpout {
    private static final String TOPIC = "paymentInfo";
    private Properties properties;
    private ConsumerConnector consumerConnector;
    private SpoutOutputCollector spoutOutputCollector;
    // ArrayBlockingQueue 是一个由数组支持的有界阻塞队列。此队列按照FIFO（先进先出）原则对元素进行排序
    // 队列的头部，是在队列中存在的时间最长的元素
    private ArrayBlockingQueue<String> paymentInfoQueue = new ArrayBlockingQueue<String>(100);

    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.spoutOutputCollector = collector;
        properties = new Properties();
        properties.put("zookeeper.connect", "zk1:2181,zk2:2181,zk3:2181");
        properties.put("group.id", "testGroup");
        properties.put("zookeeper.session.timeout.ms", "400");
        properties.put("zookeeper.sync.time.ms", "200");
        properties.put("auto.commit.interval.ms", "1000");
        consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(new ConsumerConfig(properties));

        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(TOPIC, 1);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams
                (topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(TOPIC).get(0);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            paymentInfoQueue.add(new String(it.next().message()));
        }
    }

    public void nextTuple() {
        try {
            spoutOutputCollector.emit(new Values(paymentInfoQueue.take()));
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("paymentInfo"));
    }
}