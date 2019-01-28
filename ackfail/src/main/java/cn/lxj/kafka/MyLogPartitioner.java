package cn.lxj.kafka;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MyLogPartitioner
 * description 日志分区
 * create class by lxj 2019/1/28
 **/
public class MyLogPartitioner implements Partitioner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyLogPartitioner.class);

    public MyLogPartitioner(VerifiableProperties props) {
    }

    @Override
    public int partition(Object obj, int numPartitions) {
        return Integer.parseInt(obj.toString()) % numPartitions;
    }
}
