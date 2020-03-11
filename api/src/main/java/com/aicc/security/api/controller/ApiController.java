package com.aicc.security.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@RestController
public class ApiController {

    @GetMapping("/hello")
    public String getRequest(@RequestHeader HttpHeaders headers,Principal principal) {
        Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
        entries.forEach(consumer-> System.out.println(consumer));
        return "Hello World.";
    }
}
