package cn.lxj.bigdata.storm.tmall.other;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * PaymentInfoProducer
 * description 随机生产订单消息，此服务单独部署
 * create class by lxj 2019/1/30
 **/
public class PaymentInfoProducer {
    private final static String TOPIC = "paymentInfo";

    public static void main(String[] args) {
        // 设置配置信息
        Properties props = new Properties();
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        props.put("metadata.broker.list","kafka1:9092,kafka2:9092,kafka3:9092");
        props.put("request.required.acks", "1");
        // 创建producer
        Producer<Integer, String> producer = new Producer<Integer, String>(new ProducerConfig(props));
        // 发送数据
        int messageNo = 1;
        while (true) {
            producer.send(new KeyedMessage<Integer, String>(TOPIC, PaymentInfoProducer.genPaymentInfo()));
            messageNo++;
        }
    }

    private static String genPaymentInfo() {
        return "";
    }
}