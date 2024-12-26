package com.omarcosallan.flletwise.controllers;

import com.omarcosallan.flletwise.dto.user.CreateUserDTO;
import com.omarcosallan.flletwise.dto.user.UserMinDTO;
import com.omarcosallan.flletwise.mappers.ResponseWrapper;
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
    public ResponseEntity<ResponseWrapper<UserMinDTO>> create(@RequestBody CreateUserDTO body) {
        ResponseWrapper<UserMinDTO> result = userService.create(body);
        return ResponseEntity.ok(result);
    }
}
