package cn.lxj.combinefile;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

/**
 * WholeFileRecordReader
 * description 自定义inputformate
 * create class by lxj 2018/11/20
 **/
public class WholeFileInputFormat extends FileInputFormat<NullWritable,BytesWritable>{

    /**
     * 是否要切分文件，如果是压缩文件就不切分，非压缩文件就要切分
     * @param context
     * @param filename
     * @return
     */
    @Override
    protected boolean isSplitable(JobContext context, Path filename) {
        return false;
    }

    public RecordReader<NullWritable, BytesWritable> createRecordReader(InputSplit split, TaskAttemptContext context) throws IOException, InterruptedException {
        WholeFileRecordReader reader = new WholeFileRecordReader();
        // 调用方法
        reader.initialize(split, context);
        return reader;
    }
}
