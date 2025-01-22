package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    
    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test() {
        return "Hello, World!";
    }
} 