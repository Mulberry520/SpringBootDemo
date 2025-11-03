package com.mulberry.service.impl;

import com.mulberry.entity.TbAccount;
import com.mulberry.mapper.TbAccountMapper;
import com.mulberry.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountServiceImpl implements AccountService {
    private final TbAccountMapper mapper;

    public AccountServiceImpl(TbAccountMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<TbAccount> findAll() {
        return mapper.selectAll();
    }

    @Override
    public TbAccount findById(Integer id) {
        return mapper.selectById(id);
    }

    @Override
    public TbAccount findByName(String name) {
        return mapper.selectByName2(name);
    }

    @Override
    public TbAccount findByNameAndPasswd(String name, String passwd) {
        return mapper.selectByNameAndPasswd(name, passwd);
    }

    @Override
    public boolean deleteAccount(Integer id) {
        return mapper.deleteById(id);
    }

    @Override
    public boolean addAccount(TbAccount account) {
        if (account.getId() == null) {
            return mapper.insertAccount(
                    account.getName(),
                    account.getSex(),
                    account.getPasswd(),
                    account.getEmail()
            );
        }
        return mapper.insertAccountWithId(
                account.getId(),
                account.getName(),
                account.getSex(),
                account.getPasswd(),
                account.getEmail()
        );
    }

    @Override
    public boolean changeName(String name, Integer id) {
        return mapper.updateNameById(name, id);
    }

    @Override
    public boolean changeEmail(String email, Integer id) {
        return mapper.updateEmailById(email, id);
    }
}