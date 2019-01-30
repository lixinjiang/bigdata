package cn.lxj.bigdata.storm.wordCounter;

/**
 * TestMain
 * description
 * create class by lxj 2019/1/30
 **/
public class TestMain {
    public static void main(String[] args) {
        Person person = new Person();
        person.open();
        while (true) {
            person.execute("I love China");
        }
    }
}