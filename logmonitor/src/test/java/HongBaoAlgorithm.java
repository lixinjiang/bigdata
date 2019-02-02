import java.util.Random;

/**
 * HongBaoAlgorithm
 * description 红包正态算法
 * create class by lxj 2019/2/1
 **/
public class HongBaoAlgorithm {
    static Random random = new Random();

    static {
        random.setSeed(System.currentTimeMillis());
    }

    public static void main(String[] args) {
        long max = 200;
        long min = 1;
        long[] result = HongBaoAlgorithm.generate(100_0000, 10_000, max, min);
        long total = 0;
        for (int i = 0; i < result.length; i++) {
            total += result[i];
        }
        // 检查生成的红包的总额是否正确
        System.out.println("total:" + total);
        // 统计每个钱数的红包数量，检查是否接近正太分布
        int count[] = new int[(int) max + 1];
        for (int i = 0; i < result.length; i++) {
            count[(int) result[i]] += 1;
        }
        for (int i = 0; i < count.length; i++) {
            System.out.println("" + i + "" + count[i]);
        }
    }

    static long xRandom(long min, long max) {
        return sqrt(nextLong(sqr(max - min)));
    }

    public static long[] generate(long total, int count, long max, long min) {
        long[] result = new long[count];
        long average = total / count;
        long a = average - min;
        long b = max - min;

        long range1 = sqr(average - min);
        long range2 = sqr(max - average);
        for (int i = 0; i < result.length; i++) {
            if (nextLong(min, max) > average) {
                long temp = min + xRandom(min, average);
                result[i] = temp;
                total -= temp;
            } else {
                long temp = max - xRandom(average, max);
                result[i] = temp;
                total -= temp;
            }
        }

        while (total > 0) {
            for (int i = 0; i < result.length; i++) {
                if (total > 0 && result[i] < max) {
                    result[i]++;
                    total--;
                }
            }
        }

        while (total < 0) {
            for (int i = 0; i < result.length; i++) {
                if (total < 0 && result[i] > min) {
                    result[i]--;
                    total++;
                }
            }
        }
        return result;
    }

    static long sqrt(long n) {
        return (long) Math.sqrt(n);
    }

    static long sqr(long n) {
        return n * n;
    }

    static long nextLong(long n) {
        return random.nextInt((int) n);
    }

    static long nextLong(long min, long max) {
        return random.nextInt((int) (max - min + 1)) + min;
    }
}