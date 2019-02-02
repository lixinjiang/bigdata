package cn.lxj.bigdata.log.orderMonitor.utils;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * DataSourceUtil
 * description 数据源工具类
 * create class by lxj 2019/2/1
 **/
public class DataSourceUtil {
    private static Logger logger = LoggerFactory.getLogger(DataSourceUtil.class);
    private static DataSource dataSource;

    static {
        dataSource = new ComboPooledDataSource("orderMonitor");
    }

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            dataSource = new ComboPooledDataSource();
        }
        return dataSource;
    }
}