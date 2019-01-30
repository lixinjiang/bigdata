package cn.lxj.bigdata.redis.other;

import redis.clients.jedis.Jedis;

import static cn.lxj.bigdata.redis.util.PropertiesUtil.getValue;

/**
 * RedisQuickStart
 * description
 * create class by lxj 2019/1/30
 **/
public class RedisQuickStart {
    public static void main(String[] args) {
        // 根据redis主机和端口号实例化Jedis对象
        Jedis jedis = new Jedis(getValue("redis.host"), Integer.parseInt(getValue("redis.port")));
        // 添加key-value对象，如果key对象存在就覆盖该对象
        jedis.set("name", "maxinyi");
        // 查取key的value值，如果key不存在返回null
        String name = jedis.get("name");
        String company = jedis.get("company");
        System.out.println(company + ":" + name);
        // 删除key-value对象，如果key不存在则忽略此操作
        jedis.del("name");
        // 判断key是否存在，不存在返回false存在返回true
        jedis.exists("name");
    }
}