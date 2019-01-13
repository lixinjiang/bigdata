package cn.lxj.bigdate.hive;

/**
 * Description:
 *
 * @author bonusli@163.com
 * @date 2018/12/16 23:28
 */
public class MovieBean {
    private String movieid;
    private String rate;
    private String ts;
    private String uid;

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "MovieBean{" +
                "movieid='" + movieid + '\'' +
                ", rate='" + rate + '\'' +
                ", ts='" + ts + '\'' +
                ", uid='" + uid + '\'' +
                '}';
    }
}
