package jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {
    private static final Properties PROPERTIES = new Properties();
    private static final ThreadLocal<Connection> THREAD_LOCAL = new ThreadLocal<>();

    static {
        InputStream is = JdbcUtils.class.getResourceAsStream("/jdbc.properties");
        if (is == null) {
            throw new RuntimeException("Can't find properties file");
        }
        try {
            PROPERTIES.load(is);
            Class.forName(PROPERTIES.getProperty("jdbc.driver"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        Connection connection = THREAD_LOCAL.get();
        try {
            if (connection == null){
                connection = DriverManager.getConnection(PROPERTIES.getProperty("jdbc.url"),PROPERTIES.getProperty("jdbc.username"),PROPERTIES.getProperty("jdbc.password"));
                THREAD_LOCAL.set(connection);
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return connection;
    }

    //开启事务
    public static void begin(){
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    //提交事务
    public static void commit(){
        Connection connection = null;
        try {
            connection = getConnection();
            connection.commit();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    //回滚事务
    public static void rollback(){
        Connection connection = null;
        try {
            connection = getConnection();
            connection.rollback();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }

    public static void closeAll(Connection connection, Statement statement,ResultSet resultSet){
        try {
            if (resultSet!=null){
                resultSet.close();
            }
            if (statement!=null){
                statement.close();
            }
            if (connection!=null){
                connection.close();
                THREAD_LOCAL.remove();
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
    }
}
