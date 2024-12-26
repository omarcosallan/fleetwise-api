package com.omarcosallan.flletwise.controllers;

import com.omarcosallan.flletwise.domain.user.User;
import com.omarcosallan.flletwise.dto.CreateUserDTO;
import com.omarcosallan.flletwise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<User> create(@RequestBody CreateUserDTO body) {
        User result = userService.create(body);
        return ResponseEntity.ok(result);
    }
}
