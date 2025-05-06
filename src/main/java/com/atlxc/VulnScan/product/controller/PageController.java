package com.atlxc.VulnScan.product.controller;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    @SneakyThrows
    @PreAuthorize("isAnonymous()")
    public String login(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) model.addAttribute("loginfail", "T");
        return "login";
    }

    @GetMapping("/register")
    @SneakyThrows
    @PreAuthorize("isAnonymous()")
    public String register() {
        return "register";
    }

    @GetMapping("/fail")
    @PreAuthorize("isAnonymous()")
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
