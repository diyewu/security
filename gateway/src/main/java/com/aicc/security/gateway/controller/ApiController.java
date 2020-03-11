package com.aicc.security.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ApiController {

    @GetMapping("/pri")
    public String getRequest(Principal principal) {
        System.out.println(principal);
        return "Hello World.";
    }
}
