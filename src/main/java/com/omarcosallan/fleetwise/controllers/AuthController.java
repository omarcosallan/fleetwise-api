package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.user.LoginRequestDTO;
import com.omarcosallan.fleetwise.dto.user.LoginResponseDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/sessions/password")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO body) {
        LoginResponseDTO result = authService.login(body.email(), body.password());
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/profile")
    public ResponseEntity<UserMinDTO> getProfile() {
        UserMinDTO result = authService.getProfile();
        return ResponseEntity.ok(result);
    }
}
