package com.omarcosallan.flletwise.controllers;

import com.omarcosallan.flletwise.dto.LoginRequestDTO;
import com.omarcosallan.flletwise.dto.LoginResponseDTO;
import com.omarcosallan.flletwise.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
