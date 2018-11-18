package cn.lxj.wordcount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Description:
 *
 * @author bonusli@163.com
 * @date 2018/11/6 23:26
 */
public class WordCountMapper extends Mapper<LongWritable,Text,Text,IntWritable>{
    /**
     * map节点的业务逻辑写在自定义的map（）方法中
     * maptask会对每一行输入数据调用一次我们自定义的map（）方法
     * @param key
     * @param value
     * @param context
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 将maptask传值给我们的文本内容先转换成String
        String line = value.toString();
        // 根据空格将这一行数据切分为单词
        String[] words = line.split(" ");
        for (String word : words) {
            // 强单词作为key，将参数1作为value，以便后续的数据分发，可以根据单词分发
            context.write(new Text(word), new IntWritable(1));
        }
    }
}
