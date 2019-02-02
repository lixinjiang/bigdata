package cn.lxj.bigdata.log.logMonitor.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * StringScheme
 * description  对kafka出来的数据做数据转换成字符串
 * create class by lxj 2019/1/31
 **/
public class StringScheme implements Scheme{

    @Override
    public List<Object> deserialize(byte[] bytes) {
        try {
            return new Values(new String(bytes, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("line");
    }
}