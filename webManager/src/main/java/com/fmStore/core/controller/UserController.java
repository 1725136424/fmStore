package com.fmStore.core.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
     @RequestMapping("/username")
    public Map getUsername() {
         String username = SecurityContextHolder.getContext().getAuthentication().getName();
         Map<String, String> map = new HashMap();
         map.put("username", username);
         return map;
     }
}
