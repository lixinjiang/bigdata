package cn.lxj.counter;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * MultiOutputs
 * description 通过文本的形式定义自定义计数器
 * create class by lxj 2018/12/3
 **/
public class MultiOutputs {
    // 通过枚举的形式定义自定义计数器
    enum MyCounter {
        MALFORORMED, NORMAL
    }

    static class CommaMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String[] words = value.toString().split(",");
            for (String word : words) {
                context.write(new Text(word),new LongWritable(1));
            }
            // 对枚举定义的自定义计数器加1
            context.getCounter(MyCounter.MALFORORMED).increment(1);
            // 通过动态设置自定义计数器加1
            context.getCounter("counterGroupa", "countera").increment(1);
        }
    }
}
