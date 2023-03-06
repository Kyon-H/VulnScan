package com.atlxc.VulnScan.product.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @GetMapping("/failurl")
    public String error(@NotNull Model model) {
        model.addAttribute("loginfail", "T");
        return "login";
    }

    @GetMapping("/navbar")
    public String navbar() {
        return "navbar";
    }

    @GetMapping("/ActiveScan/scans")
    public String scans() {
        return "ActiveScan/scans";
    }

    @GetMapping("/ActiveScan/vulnerabilities")
    public String vulnerabilities() {
        return "ActiveScan/vulnerabilities";
    }

    @GetMapping("/ActiveScan/vulnerabilities/detail")
    public String detail() {
        return "ActiveScan/detail";
    }

    @GetMapping("/ActiveScan/reports")
    public String reports() {
        return "ActiveScan/reports";
    }
}
