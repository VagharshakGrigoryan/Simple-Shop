package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminContoller {

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "admin");
        return "admin";
    }
}