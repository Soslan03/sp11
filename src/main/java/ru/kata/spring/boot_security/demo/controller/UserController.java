package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;


@Controller
public class UserController {
    @GetMapping("/")
    public String homePage(){
        return "index";
    }
    @GetMapping("/index")
    public String indexPage(){
        return "index";
    }
    @GetMapping("/admin")
    public String adminPage(Model model, Principal principal){
        model.addAttribute("user", principal.getName());
        return "admin";
    }
    @GetMapping("/user")
    public String showUser(Principal principal) {


        return "user";
    }

}
