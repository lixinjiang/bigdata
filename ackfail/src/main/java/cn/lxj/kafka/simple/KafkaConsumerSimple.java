package cn.lxj.kafka.simple;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * KafkaConsumerSimple
 * description
 * create class by lxj 2019/1/28
 **/
public class KafkaConsumerSimple implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerSimple.class);
    public String title;
    private KafkaStream stream;

    public KafkaConsumerSimple(String title, KafkaStream<byte[], byte[]> stream) {
        this.title = title;
        this.stream = stream;
    }

    @Override
    public void run() {
        LOGGER.info("开始运行……" + title);
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> data = it.next();
            String topic = data.topic();
            int partition = data.partition();
            long offset = data.offset();
            String msg = new String(data.message());
            LOGGER.info(String.format("Consumer: [%s],  Topic: [%s],  PartitionId: [%d], Offset: [%d], msg: " +
                    "[%s]", title, topic, partition, offset, msg));
        }
        LOGGER.info(String.format("Consumer：[%s] exiting……", title));
    }

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("group.id", "dashujujiagoushi");
        props.put("zookeeper.connect", "zk01:2181,zk02:2181,zk03:2181");
        props.put("auto.offset.reset", "largest");
        props.put("auto.commit.interval.ms", "1000");
        props.put("partition.assignment.strategy", "roundrobin");

        ConsumerConfig config = new ConsumerConfig(props);
        String topic1 = "orderMq";
        String topic2 = "paymentMq";
        //只要ConsumerConnector还在的话，consumer会一直等待新消息，不会自己退出
        ConsumerConnector consumerConn = Consumer.createJavaConsumerConnector(config);
        //定义一个map
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put(topic1, 3);

        //Map<String, List<KafkaStream<byte[], byte[]>> 中String是topic， List<KafkaStream<byte[], byte[]>是对应的流
        Map<String, List<KafkaStream<byte[], byte[]>>> topicStreamsMap = consumerConn.createMessageStreams(topicCountMap);

        //取出 `kafkaTest` 对应的 streams
        List<KafkaStream<byte[], byte[]>> streams = topicStreamsMap.get(topic1);

        //创建一个容量为4的线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);

        //创建20个consumer threads
        for (int i = 0; i < streams.size(); i++)
            executor.execute(new KafkaConsumerSimple("消费者" + (i + 1), streams.get(i)));
    }
}
