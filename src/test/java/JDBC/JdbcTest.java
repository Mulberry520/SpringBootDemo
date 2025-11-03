package JDBC;

import com.mulberry.entity.User;
import jdbc.DbService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JdbcTest {
    @Test
    public void test1() {
        DbService service = new DbService();
        List<User> users = service.getAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void test2() {
        DbService service = new DbService();
        User user = service.getById(20);
        System.out.println(user);
    }
}
