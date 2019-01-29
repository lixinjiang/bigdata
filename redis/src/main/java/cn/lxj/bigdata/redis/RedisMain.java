package cn.lxj.bigdata.redis;

import redis.clients.jedis.Jedis;

/**
 * RedisMain
 * description
 * create class by lxj 2019/1/29
 **/
public class RedisMain {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.0.99",6379);
        String response = jedis.ping();
        System.out.println(response);
    }
}
