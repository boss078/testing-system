package com.boss078.jts.controllers;

import com.boss078.jts.repositories.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("test")
public class TestController {
    @Autowired
    private TestRepository testRepository;

}