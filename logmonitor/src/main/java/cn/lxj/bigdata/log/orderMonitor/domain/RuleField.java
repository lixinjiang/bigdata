package cn.lxj.bigdata.log.orderMonitor.domain;

/**
 * RuleField
 * description
 * create class by lxj 2019/2/1
 **/
public class RuleField {
    private int id;
    private String field;
    private String desc;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RuleField{" +
                "id=" + id +
                ", field='" + field + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
