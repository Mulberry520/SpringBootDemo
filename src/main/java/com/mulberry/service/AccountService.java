package com.mulberry.service;

import com.mulberry.entity.TbAccount;

import java.util.List;

public interface AccountService {
    List<TbAccount> findAll();

    TbAccount findById(Integer id);

    TbAccount findByName(String name);

    TbAccount findByNameAndPasswd(String name, String passwd);

    boolean deleteAccount(Integer id);

    boolean addAccount(TbAccount account);

    boolean changeName(String name, Integer id);

    boolean changeEmail(String email, Integer id);
}
