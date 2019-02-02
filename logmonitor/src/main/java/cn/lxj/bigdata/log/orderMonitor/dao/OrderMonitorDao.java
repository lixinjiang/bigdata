package cn.lxj.bigdata.log.orderMonitor.dao;

import cn.lxj.bigdata.log.logMonitor.dao.DataSourceUtil;
import cn.lxj.bigdata.log.orderMonitor.domain.Condition;
import cn.lxj.bigdata.log.orderMonitor.domain.PaymentInfo;
import cn.lxj.bigdata.log.orderMonitor.domain.Product;
import cn.lxj.bigdata.log.orderMonitor.domain.Trigger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OrderMonitorDao
 * description
 * create class by lxj 2019/2/1
 **/
public class OrderMonitorDao {
    private JdbcTemplate jdbcTemplate;

    public OrderMonitorDao() {
        jdbcTemplate = new JdbcTemplate(DataSourceUtil.getDataSource());
    }

    public List<Condition> loadRules() {
        String sql = "SELECT `id`,`name`,`ruleId`,`field`,`compare`,`value` " +
                "FROM `order_monitor`.`condition_order_monitor`";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Condition.class));
    }

    public void saveTrigger(List<Trigger> list) {
        String sql = "INSERT " +
                "INTO `order_monitor`.`trigger_order_monitor` (`orderId`,`ruleId` ) " +
                "VALUES (?,?)";
        jdbcTemplate.batchUpdate(sql, list, list.size(), (ps, trigger) -> {
            ps.setString(1, trigger.getOrderId());
            ps.setInt(2, Integer.parseInt(trigger.getRuleId()));
        });
    }

    public void savePayment(PaymentInfo paymentInfo) {
        savePaymentInfo(paymentInfo);
        saveProducts(paymentInfo.getOrderId(), paymentInfo.getProducts());
    }

    private void saveProducts(final String orderId, List<Product> products) {
        String sql = "INSERT " +
                "INTO `order_monitor`.`products_order_monitor` (`orderId`,`id`,`name`,`price`,`catagory`,`promotion`," +
                "`num`) " +
                "VALUES (?,?,?,?,?,?,?) ";
        jdbcTemplate.batchUpdate(sql, products, products.size(), (ps, product) -> {
            ps.setString(1, orderId);
            ps.setString(2, product.getId());
            ps.setString(3, product.getName());
            ps.setBigDecimal(4, product.getPrice());
            ps.setString(5, product.getCatagory());
            ps.setBigDecimal(6, product.getPromotion());
            ps.setInt(7, product.getNum());
        });
    }

    private void savePaymentInfo(PaymentInfo paymentInfo) {
        String sql = "INSERT " +
                "INTO `order_monitor`.`paymentinfo_order_monitor` " +
                "(`orderId`,`createOrderTime`,`paymentId`,`paymentTime`," +
                "`shopId`,`shopName`,`shopMobile`,`ip`,`user`,`userMobile`," +
                "`address`,`addressCode`,`device`,`orderType`,`totalPrice`) " +
                "VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
        jdbcTemplate.update(sql, paymentInfo.getOrderId(), paymentInfo.getCreateOrderTime(), paymentInfo.getPaymentId
                        (), paymentInfo.getPaymentTime()
                , paymentInfo.getShopId(), paymentInfo.getShopName(), paymentInfo.getShopMobile(), paymentInfo.getIp
                        (), paymentInfo.getUser(), paymentInfo.getUserMobile()
                , paymentInfo.getAddress(), paymentInfo.getAddressCode(), paymentInfo.getDevice(), paymentInfo
                        .getOrderType(), paymentInfo.getTotalPrice());
    }

    public static void main(String[] args) {
        List<Trigger> list = new ArrayList<>();
        list.add(new Trigger("12121212", "11"));
        list.add(new Trigger("12132322", "12"));
        new OrderMonitorDao().saveTrigger(list);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setPaymentId("121212121");
        paymentInfo.setOrderId("1212121121222121");
        paymentInfo.setCreateOrderTime(new Date());
        paymentInfo.setPaymentTime(new Date());
        paymentInfo.setShopName("爱我中华");
        paymentInfo.setTotalPrice(new BigDecimal(1000));
        new OrderMonitorDao().savePayment(paymentInfo);
    }
}