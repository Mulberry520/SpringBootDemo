package com.mulberry.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TbAccount {
    private Integer id;
    private String name;
    private String sex;
    private String passwd;
    private String email;

    public TbAccount() {
    }

    public TbAccount(Integer id, String name, String sex, String passwd, String email) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.passwd = passwd;
        this.email = email;
    }
}
