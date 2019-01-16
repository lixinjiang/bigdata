package cn.lxj.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.net.TableMapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * HBaseMr
 * description
 * mapreduce操作hbase
 * create class by lxj 2019/1/15
 **/
public class HBaseMr {
    /**
     * 创建hbase配置
     */
    static Configuration config = null;

    static {
        config = HBaseConfiguration.create();
        /*过期写法，在配置文件中可配置*/
//        config.set("hbase.zookeeper.quorum", "zk1,zk2,zk3");
//        config.set("hbase.zookeeper.property.clientPort", "2181");
    }

    /**
     * 表信息
     */
    public static String tableName = "word";// 表名1
    public static String colf = "content";// 列族
    public static String col = "info";// 列
    public static String tableName2 = "stat";// 表名2

    /**
     * 初始化表结构及其数据
     */
    public static void initTB() {
        Table table = null;
//        HBaseAdmin admin = null;
        Admin admin = null;
        try {
//            admin = new HBaseAdmin(config);//创建表管理
            Connection connection = ConnectionFactory.createConnection(config);
            admin = connection.getAdmin();// 创建表管理
            /*删除表*/
            if (admin.tableExists(TableName.valueOf(tableName)) || admin.tableExists(TableName.valueOf(tableName2))) {
                System.out.println("table is already exists");
                /*删除出tableName*/
                admin.disableTable(TableName.valueOf(tableName));
                admin.deleteTable(TableName.valueOf(tableName));
                /*删除tableName2*/
                admin.disableTable(TableName.valueOf(tableName2));
                admin.deleteTable(TableName.valueOf(tableName2));
            }
            /*创建表*/
            table = connection.getTable(TableName.valueOf(tableName));
            createTable(admin, table);
            /*插入数据*/
            List<Put> lp1 = new ArrayList<Put>();
            Put p1 = new Put(Bytes.toBytes("1"));
            p1.add(colf.getBytes(), col.getBytes(), ("The Apache Hadoop software library is a framework").getBytes());
            lp1.add(p1);
            Put p2 = new Put(Bytes.toBytes("2"));
            p2.add(colf.getBytes(), col.getBytes(), ("The common utilities that support the other Hadoop modules")
                    .getBytes());
            lp1.add(p2);
            Put p3 = new Put(Bytes.toBytes("3"));
            p3.add(colf.getBytes(), col.getBytes(), ("Hadoop by reading the documentation").getBytes());
            lp1.add(p3);
            Put p4 = new Put(Bytes.toBytes("4"));
            p4.add(colf.getBytes(), col.getBytes(), ("Hadoop from the release page").getBytes());
            lp1.add(p4);
            Put p5 = new Put(Bytes.toBytes("5"));
            p5.add(colf.getBytes(), col.getBytes(), ("Hadoop on the mailing list").getBytes());
            lp1.add(p5);
            table.put(lp1);
            table.flushCommits();
            lp1.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (table != null) {
                    table.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * MyMapper继承TableMapper
     * Text:输出的key类型
     * IntWritable:输出的value类型
     */
    public static class MyMapper extends TableMapper<Text, IntWritable> {
        private static IntWritable one = new IntWritable();
        private static Text word = new Text();

        /**
         * 输出的类型
         * @param key   rowkey
         * @param value value,一行数据的Result
         * @param context
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException,
                InterruptedException {
            // 获取一行数据中的colf：col
            //  表里面只有一个列族，因而这里只获取每一行数据
            String words = Bytes.toString(value.getValue(Bytes.toBytes(colf), Bytes.toBytes(col)));
            // 按照空格分割
            String itr[] = words.toString().split(" ");
            // 循环出书word和1
            for (int i = 0; i < itr.length; i++) {
                word.set(itr[i]);
                context.write(word, one);
            }
        }
    }

    /**
     * MyReduce 继承 TableReduce
     * TableReduce<Text,IntWritable>
     * Text:输出的key类型
     * IntWritable：输入的value类型
     * ImmutableBytesWritable：输出类型，表示rowkey的类型
     */
    public static class MyReduce extends TableReducer<Text, IntWritable, ImmutableBytesWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException,
                InterruptedException {
            // 对mapper的数据求和
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            // 创建put，设置rowkey为单词
            Put put = new Put(Bytes.toBytes(key.toString()));
            // 封装数据
            put.add(Bytes.toBytes(colf), Bytes.toBytes(col), Bytes.toBytes(String.valueOf(sum)));
            // 写到hbase，需要指定rowkey、put
            context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())), put);
        }
    }

    public static void createTable(Admin admin, Table table) throws IOException {
        HTableDescriptor desc = new HTableDescriptor(table.getName());
        HColumnDescriptor family = new HColumnDescriptor(colf);
        family.setMaxVersions(3);
        family.setMinVersions(0);
        desc.addFamily(family);
        admin.createTable(desc);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        config.set("df.default.name", "hdfs://hadoop0:9000");//设置hdfs的默认路径
        config.set("hadoop.job.ugi", "hadoop,hadoop"); // 用户名，组
        config.set("mapred.job.tracker","master:9001"); // 设置jobtracker在哪
        // 初始化表
        initTB();
        // 创建job
        Job job = new Job(config, "HBaseMr");// job
        job.setJarByClass(HBaseMr.class);
        // 创建scan
        Scan scan = new Scan();
        // 可以指定查询某一列
        scan.addColumn(Bytes.toBytes(colf), Bytes.toBytes(col));
        // 创建查询Hbase的mapper，设置表名、scan、mapper类，mapper的输出key、mapper的输出value
        TableMapReduceUtil.initTableMapperJob(tableName,scan,MyMapper.class,Text.class,IntWritable.class,job);
        // 创建写入hbase的reduce，指定表名、reducer类、job
        TableMapReduceUtil.initTableReducerJob(tableName2, MyReduce.class, job);
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
