package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.user.CreateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UpdateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<UserMinDTO> create(@RequestBody CreateUserDTO body) {
        UserMinDTO result = userService.create(body);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody UpdateUserDTO body) {
        userService.update(body);
        return ResponseEntity.noContent().build();
    }
}
