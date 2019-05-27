package com.boss078.jts.controllers;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.repositories.AttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("attempt")
public class AttemptController {
    @Autowired
    private AttemptRepository attemptRepository;

    @GetMapping("{attemptId}")
    public String showAttempt(@PathVariable String attemptId, Model model){
        Optional<Attempt> byId = attemptRepository.findById(new Integer(attemptId));
        if (byId.isPresent()){
            model.addAttribute("attempt", byId.get());
            return "attempt-index";
        }
        else {
            return "404-not-found-index";
        }
    }

    @GetMapping("all")
    public String allAttempts(Model model){
        model.addAttribute("attempts", attemptRepository.findAll());
        return "attempt-all-index";
    }
}
