package cn.lxj.mr1;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * Description:
 * 将数据提交到集群中去，相当于一个yarn集群的客户端
 * 需要再次封装我们的mr程序的相关运行参数，指定jar包
 * 最后提交给yarn
 *
 * @author bonusli@163.com
 * @date 2018/11/6 23:47
 */
public class WordCountDriver {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
//        conf.set("mapreduce.framework.name","yarn");
//        conf.set("yarn.resoucemanager.hostname","hadoop0");
        Job job = Job.getInstance(conf);
        // 指定本程序的jar包所在的本地路径
        job.setJarByClass(WordCountDriver.class);

        // 指定本业务job要使用的mapper/reducer业务类
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        // 指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        // 指定最终输出数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        //指定job输入的原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        // 指定job的输出结果所在目录
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        // 将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
        /*job.submit();*/
        boolean b = job.waitForCompletion(true);
        System.exit(b ? 0 : 1);
    }
}