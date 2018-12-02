package cn.lxj.qqfensi;

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
 * 求解qq共同好友，第一步
 *
 * @author bonusli@163.com
 * @date 2018/12/2 10:17
 */
public class ShareFriendsStepOne {
    static class ShareFriendsStepOneMapper extends Mapper<LongWritable, Text, Text, Text> {
        //A:B,C,D,F,E,O
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] person_friend = line.split(":");
            String person = person_friend[0];
            String friends = person_friend[1];
            for (String friend : friends.split(",")) {
                //输出 <好友,人>
                context.write(new Text(friend), new Text(person));
            }
        }
    }

    static class ShareFirendsStepOneReducer extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text friend, Iterable<Text> persons, Context context) throws IOException, InterruptedException {
            StringBuffer buf = new StringBuffer();
            for (Text person : persons) {
                buf.append(person).append(",");
            }
            context.write(friend, new Text(buf.toString()));
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);
        job.setJarByClass(ShareFriendsStepOne.class);
        job.setMapperClass(ShareFriendsStepOneMapper.class);
        job.setReducerClass(ShareFirendsStepOneReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        FileInputFormat.setInputPaths(job, new Path("E:\\ideaprojects\\bigdata\\mapreduce\\input\\fensi\\sourcedata.txt"));
        FileOutputFormat.setOutputPath(job, new Path("E:\\ideaprojects\\bigdata\\mapreduce\\out\\fensi\\setpone.txt"));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
