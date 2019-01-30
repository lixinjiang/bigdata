package cn.lxj.bigdata.storm.wordCounter;

/**
 * Person
 * description
 * create class by lxj 2019/1/30
 **/
public class Person {
    public void open() {
        System.out.println("init.....");
    }

    public void ask() {
        System.out.println("an apple a day keeps the doctor away");
    }

    public void execute(String message) {
        System.out.println(message);
    }
}