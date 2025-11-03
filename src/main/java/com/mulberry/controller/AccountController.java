package com.mulberry.controller;

import com.mulberry.common.R;
import com.mulberry.entity.TbAccount;
import com.mulberry.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/allAccount")
    public R<List<TbAccount>> getAllUser() {
        List<TbAccount> accounts = accountService.findAll();
        return R.handlerResult(accounts, "Can't find any account");
    }

    @GetMapping("/user/{id}")
    public R<TbAccount> getUserById(@PathVariable Integer id) {
        TbAccount resAccount = accountService.findById(id);
        return R.handlerResult(resAccount, "Can't find this account");
    }

    @PostMapping("/user")
    public R<TbAccount> getUserByNameOrPasswd(
            @RequestParam("name") String name,
            @RequestParam(name = "passwd", required = false) String passwd
    ) {
        if (name == null) {
            return R.error("Request parameter 'name' can't be null");
        }

        TbAccount resAccount;
        if (passwd == null) {
            resAccount = accountService.findByName(name);
        } else {
            resAccount = accountService.findByNameAndPasswd(name, passwd);
        }

        return R.handlerResult(resAccount, "Can't find target account");
    }

    @PostMapping("/delete")
    public R<String> deleteAccount(@RequestParam("id") Integer id) {
        if (id == null) {
            return R.error("Request parameter 'id' can't be null");
        }

        if (accountService.deleteAccount(id)) {
            return R.success("Delete account success");
        }
        return R.error("Delete account failed");
    }

    @PostMapping("/add")
    public R<String> addAccount(@RequestBody TbAccount account) {
        if (account == null) {
            R.error("Request body can't be null");
        }

        if (accountService.addAccount(account)) {
            return R.success("Add account success");
        }
        return R.error("Add account failed");
    }

    @PostMapping("/user/{id}/change/name")
    public R<String> changeName(
            @PathVariable Integer id,
            @RequestParam("newName") String newName
    ) {
        TbAccount targetAccount = accountService.findById(id);
        if (targetAccount == null) {
            return R.error("Can't find account with id " + id);
        }

        if (accountService.changeName(newName, id)) {
            return R.success("Change name success");
        }
        return R.error("Change name failed");
    }

    @PostMapping("/user/{id}/change/email")
    public R<String> changeEmail(
            @PathVariable Integer id,
            @RequestParam("newEmail") String newEmail
    ) {
        TbAccount targetAccount = accountService.findById(id);
        if (targetAccount == null) {
            return R.error("Can't find account with id " + id);
        }

        if (accountService.changeEmail(newEmail, id)) {
            return R.success("Change email success");
        }
        return R.error("Change email failed");
    }
}