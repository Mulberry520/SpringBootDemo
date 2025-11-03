package com.mulberry.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String name;
    private Integer age;
    private Boolean isMarried;
    private Date birth;
    private Integer carId;

    private Car car;

    public User() {
    }

    public User(Integer id, String name, Integer age, Boolean isMarried, Date birth, Integer carId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.isMarried = isMarried;
        this.birth = birth;
        this.carId = carId;
    }
}
