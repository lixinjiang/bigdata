import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.apache.storm.guava.base.Charsets;
import org.apache.storm.guava.hash.BloomFilter;
import org.apache.storm.guava.hash.Funnel;
import org.apache.storm.guava.hash.Funnels;

/**
 * BloomFilterTest
 * description
 * create class by lxj 2019/1/18
 **/
public class BloomFilterTest {
    public static void main(String[] args) {
        long expectedInsertions = 1000_0000;
        double fpp = 0.00001;
        BloomFilter<CharSequence> bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8),
                (int) expectedInsertions, fpp);
        bloomFilter.put("aaa");
        bloomFilter.put("bbb");
        boolean containString = bloomFilter.mightContain("aaa");
        System.out.println(containString);
        BloomFilter<Email> emallBloomFilter = BloomFilter.create((Funnel<Email>) (from, into) -> into.putString(from
                .getDomain(), Charsets.UTF_8), (int) expectedInsertions, fpp);
        emallBloomFilter.put(new Email("sage.wang", "quanr.com"));
        boolean containsEmail = emallBloomFilter.mightContain(new Email("sage.wangaaa", "quanr.com"));
        System.out.println(containsEmail);
    }

    @Data
    @Builder
    @ToString
    @AllArgsConstructor
    public static class Email {
        private String userName;
        private String domain;
    }
}
