package jdbc;

import com.mulberry.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DbService implements IDbOperation{
    private Connection conn;
    @Override
    public boolean addUser(User user) {
        conn = JdbcUtils.getConnection();
        int row = 0;
        String sql = "insert into user(id, name, age, is_married, birth, car_id) values(?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);

            pStmt.setInt(1, user.getId());
            pStmt.setString(2, user.getName());
            pStmt.setInt(3, user.getAge());
            pStmt.setBoolean(4, user.getIsMarried());
            pStmt.setDate(5, user.getBirth());
            pStmt.setInt(6, user.getCarId());

            row = pStmt.executeUpdate();
            JdbcUtils.closeAll(conn, pStmt, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return row > 0;
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(Integer id) {
        return false;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        conn = JdbcUtils.getConnection();
        String sql = "select * from user";

        try{
            PreparedStatement pStmt = conn.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery();

            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                boolean isMarried = rs.getBoolean("is_married");
                Date birth = rs.getDate("birth");
                int carId = rs.getInt("car_id");

                User user = new User(id, name, age, isMarried, birth, carId);
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<User> getByName(String name) {
        return null;
    }

    @Override
    public User getById(Integer id) {
        conn = JdbcUtils.getConnection();
        String sql = "select * from user where id = ?";
        try {
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1, id);
            ResultSet rs = pStmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                boolean isMarried = rs.getBoolean("is_married");
                Date birth = rs.getDate("birth");
                int carId = rs.getInt("car_id");
                return new User(id, name, age, isMarried, birth, carId);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeAll(conn, null, null);
        }
        return null;
    }
}
