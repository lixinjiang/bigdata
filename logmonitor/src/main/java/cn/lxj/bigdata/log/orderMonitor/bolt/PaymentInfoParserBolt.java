package cn.lxj.bigdata.log.orderMonitor.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.lxj.bigdata.log.orderMonitor.domain.PaymentInfo;
import cn.lxj.bigdata.log.orderMonitor.utils.OrderMonitorHandler;

import java.util.List;

/**
 * PaymentInfoParserBolt
 * description 解析订单信息
 * create class by lxj 2019/2/1
 **/
public class PaymentInfoParserBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple input, BasicOutputCollector collector) {
        PaymentInfo paymentInfo = (PaymentInfo) input.getValueByField("paymentInfo");
        if (paymentInfo != null) {
            List<String> triggerList = OrderMonitorHandler.match(paymentInfo);
//          List<String> triggerList = new ArrayList<>();
            triggerList.add("12");
            triggerList.add("13");
            if (triggerList.size() > 0) {
                collector.emit(new Values(paymentInfo.getOrderId(), triggerList));
            }
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("orderId", "triggerList"));
    }
}