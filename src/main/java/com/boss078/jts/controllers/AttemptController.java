package com.boss078.jts.controllers;

import com.boss078.jts.entities.Attempt;
import com.boss078.jts.repositories.AttemptRepository;
import com.boss078.jts.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.tools.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("attempt")
public class AttemptController {
    @Autowired
    private AttemptRepository attemptRepository;
    @Autowired
    private TaskRepository taskRepository;


}
