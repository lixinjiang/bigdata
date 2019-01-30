package cn.lxj.bigdata.redis.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * PropertiesUtil
 * description
 * create class by lxj 2019/1/30
 **/
public class PropertiesUtil {
    private static ResourceBundle resourceBundle;

    static {
        try {
            resourceBundle = ResourceBundle.getBundle("param");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValue(String key) {
        return resourceBundle.getString(key);
    }
}
