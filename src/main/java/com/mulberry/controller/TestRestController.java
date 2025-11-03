package com.mulberry.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mulberry.common.R;
import com.mulberry.entity.Car;
import com.mulberry.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test1")
public class TestRestController {

    @RequestMapping("/add1")
    public void add1(String username, String password) {
        System.out.println("username: " + username);
        System.out.println("password: " + password);
    }

    @RequestMapping("/add2")
    public void add2(
            @RequestParam("name") String username,
            @RequestParam("passwd") String password
    ) {
        System.out.println("username: " + username);
        System.out.println("password: " + password);
    }

    @RequestMapping("/add3")
    public void add3(Car car) {
        System.out.println(car);
    }

    @RequestMapping("/add4/{username}/{password}")
    public void add4(
            @PathVariable("username") String name,
            @PathVariable("password") String passwd
    ) {
        System.out.println("user: " + name);
        System.out.println("passwd: " + passwd);
    }

    @RequestMapping("/add5")
    public void add5(HttpServletRequest req) {
        System.out.println("user: " + req.getParameter("username"));
        System.out.println("passwd: " + req.getParameter("password"));
    }

    @RequestMapping("/add6")
    public Car add6(@RequestBody Car car) {
        System.out.println(car);
        return car;
    }

    @RequestMapping("/add7")
    public User add7(@RequestBody User user) {
        System.out.println(user);
        return user;
    }

    @RequestMapping("/add8")
    public List<User> add8(@RequestBody List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
        return users;
    }

    @RequestMapping("/add9")
    public Map<String, Object> add9(@RequestBody Map<String, Object> map) {
        System.out.println(map.get("id"));
        System.out.println(map.get("name"));
        System.out.println(map.get("cars"));
        List<Object> carObject = (List<Object>) map.get("cars");
        for (Object obj : carObject) {
            if (obj instanceof Car) {
                System.out.println(obj);
            }
        }
        return map;
    }

    @RequestMapping("/delete1")
    public void delete1(@RequestParam("ids") List<Integer> ids) {
        for (Integer id : ids) {
            System.out.println(id);
        }
    }

    @RequestMapping("/car")
    public Car getCar() {
        return new Car(1001, "BMW", BigDecimal.valueOf(200000));
    }

    @RequestMapping("/string")
    public String getString() {
        String str = "{\"id\":1001, \"name\":\"BMW\", \"price\":1000000}";
        JSONObject jsonObject = JSON.parseObject(str);
        System.out.println("jsonObject = " + jsonObject);
        System.out.println("car name: " + jsonObject.getString("name"));
        return jsonObject.toJSONString();
    }

    @RequestMapping("/mapJSON")
    public R<Map<String, Object>> getMapJson() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1001);
        map.put("name", "chen");
        map.put("car", new Car(10002, "BMW", BigDecimal.valueOf(7000000)));

        return R.success(map);
    }
}
