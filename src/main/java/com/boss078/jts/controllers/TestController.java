package com.boss078.jts.controllers;

import com.boss078.jts.entities.Test;
import com.boss078.jts.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestRepository testRepository;

    @PostMapping("add")
    public String addTestDone(@ModelAttribute Test test, Model model)
    {
        testRepository.save(test);
        return "test-add-done-index";
    }

    @GetMapping("add")
    public String addTest(Model model) {
        model.addAttribute("test", new Test());
        return "test-add-index";
    }
}