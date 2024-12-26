package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.user.CreateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.UserService;
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
