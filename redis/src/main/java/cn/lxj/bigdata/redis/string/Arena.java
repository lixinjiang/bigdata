package cn.lxj.bigdata.redis.string;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * Arena
 * description 擂台
 * create class by lxj 2019/1/29
 **/
public class Arena implements Runnable {
    private Random random = new Random();
    private String redisKey;
    private Jedis jedis;
    private String arenaName;

    public Arena(String redisKey, String arenaName) {
        this.redisKey = redisKey;
        this.arenaName = arenaName;
    }

    public void run() {
        jedis = new Jedis("192.168.0.99", 6379);
        String[] daxias = new String[]{
                "郭靖", "黄蓉", "令狐冲", "杨过", "林冲", "张无忌",
                "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳",
                "王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉",
                "韦小宝", "王语嫣", "周芷若", "峨眉师太", "慕容复"
        };
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int p1 = random.nextInt(daxias.length);
            int p2 = random.nextInt(daxias.length);
            while (p1 == p2) {
                p2 = random.nextInt(daxias.length);
            }
            System.out.println("在擂台" + arenaName + "上   大侠" + daxias[p1] + " VS " + daxias[p2]);
            jedis.incr(redisKey);
        }
    }
}
