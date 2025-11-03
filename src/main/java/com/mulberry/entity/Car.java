package com.mulberry.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class Car {
    private Integer id;
    private String name;
    private BigDecimal price;

    public Car() {
    }

    public Car(Integer id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}
