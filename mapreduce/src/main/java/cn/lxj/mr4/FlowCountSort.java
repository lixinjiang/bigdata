package cn.lxj.mr4;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Description:
 *
 * @author bonusli@163.com
 * @date 2018/11/14 22:32
 */
public class FlowCountSort {
    static class FlowCountSortMapper extends Mapper<LongWritable, Text, FlowBean, Text> {
        // 由于每次数据都会被序列化，所以不存在地址的值一直被最后一个覆盖的情况
        FlowBean bean = new FlowBean();
        Text v = new Text();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 拿到的是上一个统计程序的输出结果，已经是各手机号的总流量信息
            String line = value.toString();
            String[] fields = line.split("\t");
            String phone = fields[0];
            long upFlow = Long.parseLong(fields[1]);
            long dFlow = Long.parseLong(fields[2]);
            bean.set(upFlow, dFlow);
            v.set(phone);
            context.write(bean, v);
        }
    }

    /**
     * 根据key来掉, 传过来的是对象, 每个对象都是不一样的, 所以每个对象都调用一次reduce方法
     */
    static class FlowCountSortReducer extends Reducer<FlowBean, Text, Text, FlowBean> {
        @Override
        protected void reduce(FlowBean bean, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            context.write(values.iterator().next(), bean);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        //指定本程序的jar包所在的本地路径
        job.setJarByClass(FlowCountSort.class);

        //指定本业务job要使用的mapper/Reducer业务类
        job.setMapperClass(FlowCountSortMapper.class);
        job.setReducerClass(FlowCountSortReducer.class);

        //指定mapper输出数据的kv类型
        job.setMapOutputKeyClass(FlowBean.class);
        job.setMapOutputValueClass(Text.class);

        //指定最终输出的数据的kv类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        //指定job的输入原始文件所在目录
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        //指定job的输出结果所在目录
        Path outPath = new Path(args[1]);
        /*FileSystem fs = FileSystem.get(conf);
		if(fs.exists(outPath)){
			fs.delete(outPath, true);
		}*/
        FileOutputFormat.setOutputPath(job, outPath);
        //将job中配置的相关参数，以及job所用的java类所在的jar包，提交给yarn去运行
        /*job.submit();*/
        boolean res = job.waitForCompletion(true);
        System.exit(res?0:1);
    }
}
