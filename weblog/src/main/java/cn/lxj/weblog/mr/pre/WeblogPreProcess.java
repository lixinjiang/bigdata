package cn.lxj.weblog.mr.pre;

import cn.lxj.weblog.mrbean.WebLogBean;
import cn.lxj.weblog.mrbean.WebLogParser;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * WeblogPreProcess
 * description
 * 处理原始日志，过滤出真实PV请求
 * 转换时间格式
 * 对缺失字段进行填充默认值
 * 对记录标记valid和invalid
 * create class by lxj 2019/1/9
 **/
public class WeblogPreProcess {
    static class WeblogPreProcessMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        // 用来存储网站URL分类数据
        Set<String> pages = new HashSet<String>();
        Text k = new Text();
        NullWritable v = NullWritable.get();

        /**
         * 从外部加载网站URL分类数据
         *
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            super.setup(context);
            pages.add("/about");
            pages.add("/black-ip-list/");
            pages.add("/cassandra-clustor/");
            pages.add("/finance-rhive-repurchase/");
            pages.add("/hadoop-family-roadmap/");
            pages.add("/hadoop-hive-intro/");
            pages.add("/hadoop-zookeeper-intro/");
            pages.add("/hadoop-mahout-roadmap/");
        }

        /**
         * mr处理流程
         *
         * @param key
         * @param value
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            super.map(key, value, context);
            String line = value.toString();
            WebLogBean webLogBean = WebLogParser.parser(line);
            // 过滤js/图片/css等静态资源
            WebLogParser.filtStaticResource(webLogBean, pages);
            /* if (!webLogBean.isValid()) return; */
            k.set(webLogBean.toString());
            context.write(k, v);
        }
    }

    //RUN|DEBUG
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(WeblogPreProcess.class);
        job.setMapperClass(WeblogPreProcessMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);
        FileInputFormat.setInputPaths(job, new Path("/"));
        FileOutputFormat.setOutputPath(job, new Path("/"));
        job.setNumReduceTasks(0);
        job.waitForCompletion(true);
    }
}
