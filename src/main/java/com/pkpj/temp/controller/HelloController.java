package com.pkpj.temp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pkpj.temp.dtos.Message;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public Message sayHello() {
        return new Message("Hello, World!");
    }
    @PostMapping("/helloworldss")
    public Message postHello(@RequestBody Message message) {
    return message;
}
}
