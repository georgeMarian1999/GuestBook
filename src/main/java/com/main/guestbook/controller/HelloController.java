package com.main.guestbook.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    @GetMapping("/")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to azure";
    }
}
