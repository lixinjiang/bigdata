package cn.lxj.bigdata.redis.other;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static cn.lxj.bigdata.redis.util.PropertiesUtil.getValue;
/**
 * MyJedisPool
 * description jedis线程池
 * create class by lxj 2019/1/30
 **/
public class MyJedisPool {
    // jedis 池
    public static JedisPool pool;

    // 初始化线程池
    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);// 最大空闲数
        config.setMaxTotal(1000 * 100);
        config.setMaxWaitMillis(5);
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        try {
            //如果你遇到 java.net.SocketTimeoutException: Read timed out exception的异常信息
            //请尝试在构造JedisPool的时候设置自己的超时值. JedisPool默认的超时时间是2秒(单位毫秒)
            pool = new JedisPool(config, getValue("redis.host"), Integer.parseInt(getValue("redis.port")), 20);
        } catch (Exception e) {
            throw new RuntimeException("redis 连接池初始化失败！");
        }
    }

    public static void main(String[] args) {
        // 从jedis池中获取一个jedis实例
        Jedis jedis = MyJedisPool.pool.getResource();
        // 添加key-value对象，如果key对象存在就覆盖该对象
        jedis.set("name", "lixinjiang");
        jedis.set("company", "HRT");
        // 查取key的value值，如果key不存在返回null
        String name = jedis.get("name");
        String company = jedis.get("company");
        System.out.println(company + ":" + name);
        // 删除key-value对象，如果key不存在则忽略此操作
        jedis.del("name");
        // 判断key是否存在，不存在返回false存在返回true
        jedis.exists("name");
        //关闭jedis链接，自动回收
        jedis.close();
    }
}
