package com.boss078.jts.controllers;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.entities.Test;
import com.boss078.jts.repositories.AttemptRepository;
import com.boss078.jts.repositories.TaskRepository;
import com.boss078.jts.entities.Task;
import com.boss078.jts.repositories.TestRepository;
import com.boss078.jts.service.attemptServing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("task")
public class TaskController {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private AttemptRepository attemptRepository;

    @PostMapping("add")
    public String addTaskDone(@ModelAttribute Task task, Model model)
    {
        taskRepository.save(task);
        model.addAttribute("task", task);
        return "task-add-done-index";
    }

    @GetMapping("add")
    public String addTask(Model model) {
        model.addAttribute("task", new Task());
        return "task-add-index";
    }

    @GetMapping("{taskId}")
    public String showTask(@PathVariable String taskId, Model model){
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()){
            model.addAttribute("task", byId.get());
            return "task-index";
        }
        else {
            return "404-not-found-index";
        }
    }

    @GetMapping("all")
    public String allTasks(Model model){
        model.addAttribute("tasks", taskRepository.findAll());
        return "task-all-index";
    }

    @PostMapping("{taskId}/test/add")
    public String addTestDone(@PathVariable String taskId, @ModelAttribute Test test, Model model) {
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()) {
            test.setTask(byId.get());
            byId.get().getTests().add(test);
            model.addAttribute("test", test);
            testRepository.save(test);
            return "test-add-done-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @GetMapping("{taskId}/test/add")
    public String addTest(@PathVariable String taskId, Model model) {
        if (taskRepository.findById(new Integer(taskId)).isPresent()) {
            model.addAttribute("test", new Test());
            model.addAttribute("taskId", taskId);
            return "test-add-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @GetMapping("{taskId}/test/all")
    public String allTests(@PathVariable String taskId, Model model){
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()){
            model.addAttribute("tests", testRepository.findAllByTask(byId.get()));
            model.addAttribute("taskId", taskId);
            return "test-all-index";
        }
        else {
            return "404-not-found-index";
        }
    }

    @GetMapping("{taskId}/attempt/add")
    public String addAttempt(@PathVariable String taskId, Model model) {
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()) {
            Attempt attempt = new Attempt();
            attempt.setTask(byId.get());
            model.addAttribute("attempt", attempt);
            model.addAttribute("taskId", taskId);
            return "attempt-add-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @PostMapping("{taskId}/attempt/add")
    public String addAttemptDone(@PathVariable String taskId, @ModelAttribute Attempt attempt, Model model) {
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()) {
            attempt.setTask(byId.get());
            attemptServing classServing = new attemptServing();
            attempt = classServing.compileAndRun(attempt);
            model.addAttribute("attempt", attempt);
            byId.get().getAttempts().add(attempt);
            if (attempt.getErrorCode() == 0) {
                attempt.setResult("Completed");
            }
            else {
                attempt.setResult("Not completed");
            }
            attemptRepository.save(attempt);
            return "attempt-add-done-index";
        }
        else {
            return "404-not-found-index.html";
        }
    }

    @GetMapping("{taskId}/attempt/all")
    public String allAttemptsOfTask(@PathVariable String taskId, Model model){
        Optional<Task> byId = taskRepository.findById(new Integer(taskId));
        if (byId.isPresent()){
            model.addAttribute("attempts", attemptRepository.findAllByTask(byId.get()));
            return "attempt-all-index";
        }
        else {
            return "404-not-found-index";
        }
    }
}
