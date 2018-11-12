package cn.lxj.mr3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * Description:
 * K2  V2  对应的是map输出kv的类型
 *
 * @author bonusli@163.com
 * @date 2018/11/13 0:24
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean> {
    public static HashMap<String, Integer> proviceDict = new HashMap<String, Integer>();

    static {
        proviceDict.put("136", 0);
        proviceDict.put("137", 1);
        proviceDict.put("138", 2);
        proviceDict.put("139", 3);
    }

    public int getPartition(Text key, FlowBean value, int numPartitions) {
        String prefix = key.toString().substring(0, 3);
        Integer provinceId = proviceDict.get(prefix);

        return provinceId == null ? 4 : provinceId;
    }
}
