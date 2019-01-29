package cn.lxj.bigdata.redis.string;

import java.io.Serializable;

/**
 * Person
 * description TODO
 * create class by lxj 2019/1/29
 **/
public class Person implements Serializable {
    private static final long serialVersionUID = -9012113097419111583L;
    private String name;
    private int age;

    public Person() {
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
