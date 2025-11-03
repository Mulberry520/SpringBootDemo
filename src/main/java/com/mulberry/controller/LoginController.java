package com.mulberry.controller;

import com.mulberry.entity.TbAccount;
import com.mulberry.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
    private final AccountService accountService;

    public LoginController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("doLogin")
    public String doLogin(
            @RequestParam("username") String name,
            @RequestParam("password") String passwd,
            HttpSession session,
            RedirectAttributes  redirectAttributes
    ) {
        TbAccount account = accountService.findByNameAndPasswd(name, passwd);
        if (account != null) {
            session.setAttribute("loginUser", name);
            return "redirect:/profile";
        }
        redirectAttributes.addFlashAttribute("error", "Wrong name or password");
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        return "profile";
    }
}
