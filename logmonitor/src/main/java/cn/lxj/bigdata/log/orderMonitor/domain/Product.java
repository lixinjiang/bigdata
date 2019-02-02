package cn.lxj.bigdata.log.orderMonitor.domain;

import java.math.BigDecimal;

/**
 * Product
 * description
 * create class by lxj 2019/2/1
 **/
public class Product {
    private String id;//商品编号
    private String name;//商品名称
    private BigDecimal price;//商品价格
    private String catagory;//商品分类
    private BigDecimal promotion;//促销价格
    private int num;//商品数量

    public Product() {
    }

    public Product(String id, String name, BigDecimal price, String catagory, BigDecimal promotion, int num) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.catagory = catagory;
        this.promotion = promotion;
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getPromotion() {
        return promotion;
    }

    public void setPromotion(BigDecimal promotion) {
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCatagory() {
        return catagory;
    }

    public void setCatagory(String catagory) {
        this.catagory = catagory;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", catagory='" + catagory + '\'' +
                ", promotion=" + promotion +
                ", num=" + num +
                '}';
    }
}
