package cn.lxj.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.awt.event.FocusEvent;
import java.io.IOException;
import java.util.ArrayList;

/**
 * HbaseTest
 * description 测试类
 * create class by lxj 2019/1/16
 **/
public class HbaseTest {
    static Configuration config = null;
    private Connection connection = null;
    private Table table = null;

    @Before
    public void init() throws Exception {
        config = HBaseConfiguration.create();// 配置
        config.set("hbase.zookeeper.quorum", "master,work1,work2");// Zookeeper地址
        config.set("hbase.zookeeper.property.ClientPort", "2181"); // Zookeeper端口
        connection = ConnectionFactory.createConnection(config);
        table = connection.getTable(TableName.valueOf("user"));
    }

    /**
     * 创建一个表
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("deprecation")
    public void createTable() throws Exception {
        // 创建表管理类
        HBaseAdmin admin = new HBaseAdmin(config);// hbase表管理
        // 创建表描述类
        TableName tableName = TableName.valueOf("test3"); // 表名称
        HTableDescriptor desc = new HTableDescriptor(tableName);
        // 创建列族的描述类
        HColumnDescriptor family1 = new HColumnDescriptor("info");
        // 将列族添加到表中
        desc.addFamily(family1);
        HColumnDescriptor family2 = new HColumnDescriptor("info");
        // 将列族添加到表中
        desc.addFamily(family2);
        // 创建表
        admin.createTable(desc);
    }

    /**
     * 删除表
     *
     * @throws Exception
     */
    @Test
    @SuppressWarnings("deprecation")
    public void deleteTable() throws Exception {
        HBaseAdmin admin = new HBaseAdmin(config);
        admin.disableTable("test3");
        admin.deleteTable("test3");
    }

    @Test
    @SuppressWarnings({"deprecation", "resource"})
    public void insertData() throws IOException {
        table.setAutoFlushTo(false);
        table.setWriteBufferSize(534534534);
        ArrayList<Put> arrayList = new ArrayList<Put>();
        for (int i = 21; i < 59; i++) {
            Put put = new Put(Bytes.toBytes("1234" + 1));
            put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("apple" + i));
            put.add(Bytes.toBytes("info"), Bytes.toBytes("password"), Bytes.toBytes(1234 + i));
            arrayList.add(put);
        }
        table.put(arrayList);
        table.flushCommits();
    }

    /**
     * 修改数据
     *
     * @throws Exception
     */
    @Test
    public void updateData() throws Exception {
        Put put = new Put(Bytes.toBytes("1234"));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("namessss"), Bytes.toBytes("lisi1234"));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("password"), Bytes.toBytes(1234));
        // 插入数据
        table.put(put);
        // 提交
        table.flushCommits();
    }

    /**
     * 单条查询
     *
     * @throws Exception
     */
    @Test
    public void queryData() throws Exception {
        Get get = new Get(Bytes.toBytes("1234"));
        Result result = table.get(get);
        System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
        System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("namessss"))));
        System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex"))));
    }

    /**
     * 全表扫描
     *
     * @throws Exception
     */
    @Test
    public void scanData() throws Exception {
        Scan scan = new Scan();
        scan.setStartRow(Bytes.toBytes("wangsf_0"));
        scan.setStopRow(Bytes.toBytes("wangwu"));
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
        }
    }

    /**
     * 全表扫描过滤器
     *
     * @throws Exception
     */
    @Test
    public void scanDataByFilter() throws Exception {
        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 过滤器：列值过滤器
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("info"),
                Bytes.toBytes("name"), CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("zhangsan2"));
        // 设置过滤器
        scan.setFilter(filter);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
        }
    }


    /**
     * rowkey过滤器
     *
     * @throws Exception
     */
    @Test
    public void scanDataByFilter2() throws Exception {
        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 匹配rowkey以lxj开头的
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^12341"));
        // 设置过滤器
        scan.setFilter(filter);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println(Bytes.toInt(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("password"))));
            System.out.println(Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"))));
        }
    }

    /**
     * 匹配列名前缀
     *
     * @throws Exception
     */
    @Test
    public void scanDataByFilter3() throws Exception {
        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 匹配rowkey以wangsenfeng开头
        ColumnPrefixFilter filter = new ColumnPrefixFilter(Bytes.toBytes("na"));
        // 设置过滤器
        scan.setFilter(filter);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println("rowkey：" + Bytes.toString(result.getRow()));
            System.out.println("info：name：" + Bytes.toString(result.getValue(Bytes.toBytes("info"),
                    Bytes.toBytes("name"))));
            // 判断取出来的结果是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")) != null) {
                System.out.println("info:age：" + Bytes.toInt(result.getValue(Bytes.toBytes("info"),
                        Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex")) != null) {
                System.out.println("infi:sex："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info"),
                        Bytes.toBytes("sex"))));
            }
            // 判断取出来的值是否为空·
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name")) != null) {
                System.out
                        .println("info2:name："
                                + Bytes.toString(result.getValue(
                                Bytes.toBytes("info2"),
                                Bytes.toBytes("name"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age")) != null) {
                System.out.println("info2:age："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
                        Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex")) != null) {
                System.out.println("info2:sex："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
                        Bytes.toBytes("sex"))));
            }
        }
    }

    /**
     * 过滤器集合
     *
     * @throws Exception
     */
    public void scanDataByFilter4() throws Exception {
        // 创建全表扫描的scan
        Scan scan = new Scan();
        // 过滤器集合    MUST_PASS_ALL（and）,MUST_PASS_ONE(or)
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL);
        // 匹配rowkey以lxj开头的
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.EQUAL, new RegexStringComparator("^lxj"));
        // 匹配name值等于lxj
        SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL, Bytes.toBytes("lxj"));
        filterList.addFilter(filter);
        filterList.addFilter(filter2);
        // 设置过滤器
        scan.setFilter(filterList);
        // 打印结果集
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            System.out.println("rowkey:" + Bytes.toString(result.getRow()));
            System.out.println("info:name:" + Bytes.toString(result.getValue(Bytes.toBytes("info"), Bytes.toBytes
                    ("name"))));
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")) != null) {
                System.out.println("info:age："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info"),
                        Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex")) != null) {
                System.out.println("infi:sex："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info"),
                        Bytes.toBytes("sex"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name")) != null) {
                System.out
                        .println("info2:name："
                                + Bytes.toString(result.getValue(
                                Bytes.toBytes("info2"),
                                Bytes.toBytes("name"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age")) != null) {
                System.out.println("info2:age："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
                        Bytes.toBytes("age"))));
            }
            // 判断取出来的值是否为空
            if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex")) != null) {
                System.out.println("info2:sex："
                        + Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
                        Bytes.toBytes("sex"))));
            }
        }
    }

    @Test
    public void close() throws IOException {
        table.close();
        connection.close();
    }
}