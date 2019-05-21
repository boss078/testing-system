package com.boss078.jts.controllers;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.entities.Test;
import com.boss078.jts.repositories.TaskRepository;
import com.boss078.jts.entities.Task;
import com.boss078.jts.service.attemptServing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("task/{taskId}")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;

    @PostMapping("add")
    public String addTaskDone(@ModelAttribute Task task, Model model)
    {
        taskRepository.save(task);
        return "task-add-done-index";
    }

    @PostMapping("test/add")
    public String addTestDone(@PathVariable String taskId, @ModelAttribute Test test, Model model) {
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()) {
            test.setTask(byId.get());
            byId.get().getTests().add(test);
            return "test-add-done-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @GetMapping("test/add")
    public String addTest(@PathVariable String taskId, Model model) {
        model.addAttribute("test", new Test());
        if (taskRepository.findById(new Integer(taskId)).isPresent()) {
            return "test-add-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }


    @GetMapping("attempt/add")
    public String addAttempt(@PathVariable String taskId, Model model) {
        model.addAttribute("attempt", new Attempt());
        if (taskRepository.findById(new Integer(taskId)).isPresent()) {
            return "attempt-add-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @PostMapping("attempt/add")
    public String addAttemptDone(@PathVariable String taskId, @ModelAttribute Attempt attempt, Model model) {
        if (taskRepository.findById(new Integer(taskId)).isPresent()) {
            attemptServing classServing = new attemptServing();
            attempt = classServing.compileAndRun(attempt);
            model.addAttribute("attempt", attempt);
            return "attempt-add-done-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @GetMapping("add")
    public String addTask(Model model) {
        model.addAttribute("task", new Task());
        return "task-add-index";
    }

}
