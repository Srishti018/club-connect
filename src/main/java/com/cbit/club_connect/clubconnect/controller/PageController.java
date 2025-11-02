package com.cbit.club_connect.clubconnect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    // Redirects /signup → static signup.html
    @GetMapping("/signup")
    public String signupPage() {
        return "forward:/signup.html";
    }

    // Redirects /login → static login.html
    @GetMapping("/login")
    public String loginPage() {
        return "forward:/login.html";
    }

    // Handles /register.html → static registration.html
    @GetMapping("/register.html")
    public String alias() {
        return "forward:/registration.html";
    }

    // Student Dashboard
    @GetMapping("/student-dashboard")
    public String studentDashboard() {
        return "forward:/student-dashboard.html";
    }

    // Club Dashboard (NEW)
    @GetMapping("/club-dashboard")
    public String clubDashboard() {
        return "forward:/club-dashboard.html";
    }
}
