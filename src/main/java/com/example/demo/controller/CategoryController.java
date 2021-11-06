package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.repasitory.CategoryRepository;
import com.example.demo.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryServiceImpl categoryService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/allCategories")
    public String getAllCategory(ModelMap modelMap) {
        List<Category> categories = categoryRepository.findAll();
        modelMap.addAttribute("categorys", categories);
        return "categorys";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/allCategories";
    }

    @GetMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/allCategories";
    }
}
