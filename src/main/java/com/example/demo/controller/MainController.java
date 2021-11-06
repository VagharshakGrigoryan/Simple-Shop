package com.example.demo.controller;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About us");
        return "about";
    }
    @RequestMapping("/home")
    public String viewHomePage(Model model, @Param("keyword") String keyword) {
        model.addAttribute("keyword", keyword);

        return "products";
    }
    @GetMapping("/403")
    public String forbidden() {
        return "403";
    }
}
