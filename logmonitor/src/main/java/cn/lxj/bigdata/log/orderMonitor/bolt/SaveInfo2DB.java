package cn.lxj.bigdata.log.orderMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.lxj.bigdata.log.orderMonitor.domain.PaymentInfo;
import cn.lxj.bigdata.log.orderMonitor.utils.OrderMonitorHandler;

import java.util.List;

/**
 * SaveInfo2DB
 * description
 * create class by lxj 2019/2/1
 **/
public class SaveInfo2DB extends BaseBasicBolt {
    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        String firstField = input.getFields().get(0);
        if ("orderId".equals(firstField)) {
            OrderMonitorHandler.saveTrigger(input.getStringByField("orderId"), (List<String>)input.getValueByField("triggerList"));
        }
        if ("paymentInfo".equals(firstField)) {
            OrderMonitorHandler.savePaymentInfo((PaymentInfo) input.getValueByField("paymentInfo"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 不需要往外发数据，不需定义发出去的数据字段名
    }
}