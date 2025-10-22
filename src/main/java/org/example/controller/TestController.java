package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("/hello")
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "hello world");
        response.put("version", "1.0");
        return response;
    }
}
