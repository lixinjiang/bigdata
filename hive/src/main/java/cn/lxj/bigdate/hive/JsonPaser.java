package cn.lxj.bigdate.hive;

import org.apache.hadoop.hive.ql.exec.UDF;
import parquet.org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
 * Description:
 *
 * @author bonusli@163.com
 * @date 2018/12/16 23:25
 */
public class JsonPaser extends UDF {
    public String evaluate(String jsonline) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            MovieBean movieBean = mapper.readValue(jsonline, MovieBean.class);
            return movieBean.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
