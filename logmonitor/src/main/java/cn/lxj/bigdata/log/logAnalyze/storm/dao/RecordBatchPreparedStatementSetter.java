package cn.lxj.bigdata.log.logAnalyze.storm.dao;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * RecordBatchPreparedStatementSetter
 * description
 * create class by lxj 2019/1/31
 **/
public class RecordBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
    private Map<String, Map<String, Object>> appData;

    @Override
    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {

    }

    @Override
    public int getBatchSize() {
        return appData.get("pv").size();
    }
}