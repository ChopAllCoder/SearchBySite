package util;

import conf.Constant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    public static Connection conn = null;


    //单例模式，使对象唯一性
    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }
        try {
            //加载驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
            //获取连接
            String URL = "jdbc:mysql://127.0.0.1:3306/" + Constant.DB_NAME
                    + "?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
            conn = DriverManager.getConnection(URL, Constant.DB_USER, Constant.DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) {
    }
}