package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class TestController {

    @GetMapping("/hello")
    public HashMap<String,Object> sayHello() {
        HashMap<String,Object> obj = new HashMap<>();
        obj.put("Message","Hello world");
        return obj;
    }
}
