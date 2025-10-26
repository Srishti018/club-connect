package com.cbit.club_connect.clubconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // maps to templates/signup.html
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";  // maps to templates/login.html
    }
}
