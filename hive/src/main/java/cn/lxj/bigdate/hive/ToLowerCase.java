package cn.lxj.bigdate.hive;

import org.apache.hadoop.hive.ql.exec.UDF;

import java.util.HashMap;

/**
 * Description:
 *
 * @author bonusli@163.com
 * @date 2018/12/16 22:43
 */
public class ToLowerCase extends UDF {

    public static HashMap<String, String> provinceMap = new HashMap<String, String>();

    static {
        provinceMap.put("136", "北京");
        provinceMap.put("137", "上海");
        provinceMap.put("138", "太原");
    }

    /**
     * 转换小写,必须是public
     *
     * @param field
     * @return
     */
    public String evaluate(String field) {
        return field.toLowerCase();
    }

    public String evalaute(int phoneNumber) {
        return provinceMap.containsKey(String.valueOf(phoneNumber).substring(0, 3)) ? "火星" : provinceMap.get(String.valueOf(phoneNumber).substring(0, 3));
    }
}
