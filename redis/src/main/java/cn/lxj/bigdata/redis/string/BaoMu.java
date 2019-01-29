package cn.lxj.bigdata.redis.string;

import redis.clients.jedis.Jedis;

/**
 * BaoMu
 * description 报幕
 * create class by lxj 2019/1/29
 **/
public class BaoMu implements Runnable {
    private Jedis jedis;
    private String redisKey;
    public BaoMu(String redisKey) {
        this.redisKey = redisKey;
    }

    public void run() {
        jedis = new Jedis("192.168.0.99", 6379);
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("===================当前总共比武次数为：" + jedis.get(redisKey));
            } catch (Exception e) {
                System.out.println("擂台被损坏..."+e);
            }
        }
    }
}
