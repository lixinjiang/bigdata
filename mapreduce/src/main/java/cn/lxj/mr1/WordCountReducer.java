package cn.lxj.mr1;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Description:
 * KEYVIN,VALUEIN和maptask的输出相同
 *
 * @author bonusli@163.com
 * @date 2018/11/6 23:34
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    /**
     * @param key     是一组相同单词kv对的key
     * @param values
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable value : values) {
            count += value.get();

        }
        context.write(key, new IntWritable(count));
    }
}
