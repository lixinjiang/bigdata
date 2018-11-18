/**
 * test
 * description TODO
 * create class by lxj 2018/11/16
 **/
public class test {
    public static void main(String[] args) {
        // first check the Dflag hadoop.home.dir with JVM scope
//        String home = System.getProperty("hadoop.home.dir");
//        System.out.println(home);
        String line = "1001 20150710 P0001 2 ";
        String[] split = line.split(" ");
        System.out.println(split.length);

    }
}
