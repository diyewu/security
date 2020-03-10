package com.aicc.security.uaa.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class OAuthResourceController {

    @PostMapping("/api/hi")
    public String say(String name) {
        return "hi , " + name;
    }
}
