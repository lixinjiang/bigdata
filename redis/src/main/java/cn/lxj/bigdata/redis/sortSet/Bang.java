package cn.lxj.bigdata.redis.sortSet;

import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static cn.lxj.bigdata.redis.util.PropertiesUtil.getValue;

/**
 * Bang
 * description
 * create class by lxj 2019/1/30
 **/
public class Bang {
    public static void main(String[] args) {
        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 创建销售线程-销售商品
        executorService.submit(new Sale());
        executorService.submit(new Sale());
        // 创建报表线程-周期型计算排行榜
        executorService.submit(new BangView());
    }
}

class Sale implements Runnable {
    //店铺销售排行榜
    private static final String amountBang = "tmall:amountBang";
    //店铺订单排行榜
    private static final String orderBang = "tmall:orderBang";
    //店铺名称
    private static final String[] shops = new String[]{"小米", "华为", "魅族", "苹果", "联想", "奇酷", "中兴", "一加", "oppo"};
    //Redis客户端
    Jedis jedis = new Jedis(getValue("redis.host"), Integer.parseInt(getValue("redis.port")));
    //随机获取店铺
    private Random random = new Random();
    //随机计算价格
    private Random priceRandom = new Random();

    public void run() {
        while (true) {
            int shopIndex = random.nextInt(shops.length);
            jedis.zincrby(amountBang, priceRandom.nextInt(2500), shops[shopIndex]);
            jedis.zincrby(orderBang, 1, shops[shopIndex]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class BangView implements Runnable {
    // 店铺销售排行榜
    private static final String AMOUNTBANG = "tmall:amountBang";
    // 店铺订单排行榜
    private static final String ORDERBANG = "tmall:orderBang";
    // Redis客户端
    Jedis jedis = new Jedis(getValue("redis.host"), Integer.parseInt(getValue("redis.port")));

    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                System.out.println("==================店铺销售额排行================");
                Set<String> names = jedis.zrevrange(AMOUNTBANG, 0, -4);
                for (String name : names) {
                    System.out.println(name + "         "
                            + jedis.zrevrank(AMOUNTBANG, name) + "            "
                            + jedis.zscore(ORDERBANG, name));
                }
                System.out.println("==============店铺订单量排行=====================");
                names = jedis.zrevrange(ORDERBANG, 0, 1);
                for (String name : names) {
                    System.out.println(name + "         "
                            + jedis.zrevrank(ORDERBANG, name) + "            "
                            + jedis.zscore(ORDERBANG, name));
                }
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}