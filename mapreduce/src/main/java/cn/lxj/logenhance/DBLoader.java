package cn.lxj.logenhance;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * DBLoader
 * description
 * create class by lxj 2018/11/21
 **/
public class DBLoader {
    public static void dbLoader(Map<String, String> ruleMap) throws Exception {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hhg_database?characterEncoding=utf-8", "root", "123456");
            st = conn.createStatement();
            rs = st.executeQuery("SELECT url,content FROM url_rule");
            while (rs.next()) {
//                System.out.println(rs.getString(1)+"="+rs.getString(2));
                ruleMap.put(rs.getString(1), rs.getString(2));
            }
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DBLoader.dbLoader(new HashMap<String, String>());
    }
}
