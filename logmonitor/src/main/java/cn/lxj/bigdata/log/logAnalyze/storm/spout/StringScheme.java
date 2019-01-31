package cn.lxj.bigdata.log.logAnalyze.storm.spout;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import java.util.List;

/**
 * StringScheme
 * description
 * create class by lxj 2019/1/31
 **/
public class StringScheme implements Scheme {

    @Override
    public List<Object> deserialize(byte[] bytes) {
        try {
            return new Values(new String(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("line");
    }
}