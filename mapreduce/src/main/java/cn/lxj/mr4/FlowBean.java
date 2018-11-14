package cn.lxj.mr4;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Description:
 * 实现可排序的flowbean，在maptask阶段进行调整，key从phone改为flowbean
 * @author bonusli@163.com
 * @date 2018/11/14 22:18
 */
public class FlowBean implements WritableComparable<FlowBean> {
    private long upFlow;
    private long dFlow;
    private long sumFlow;

    //反序列化时，需要反射调用空参构造函数，所以要显示定义一个
    public FlowBean(){}
    public FlowBean(long upFlow, long dFlow) {
        this.upFlow = upFlow;
        this.dFlow = dFlow;
        this.sumFlow = upFlow + dFlow;
    }

    public void set(long upFlow,long dFlow) {
        this.upFlow = upFlow;
        this.dFlow = dFlow;
        this.sumFlow = upFlow + dFlow;
    }
    @Override
    public String toString() {
        return upFlow + "\t" + dFlow + "\t" + sumFlow;
    }

    public int compareTo(FlowBean bean) {
        return this.sumFlow > bean.sumFlow ? -1 : 1;
    }

    /**
     * 序列化方法
     * @param dataOutput
     * @throws IOException
     */
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeLong(upFlow);
        dataOutput.writeLong(dFlow);
        dataOutput.writeLong(sumFlow);
    }

    /**
     * 反序列化方法
     * @param dataInput
     * @throws IOException
     */
    public void readFields(DataInput dataInput) throws IOException {
        upFlow = dataInput.readLong();
        dFlow = dataInput.readLong();
        sumFlow = dataInput.readLong();
    }
}
