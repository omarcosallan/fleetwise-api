package com.omarcosallan.flletwise.controllers;

import com.omarcosallan.flletwise.dto.user.LoginRequestDTO;
import com.omarcosallan.flletwise.dto.user.LoginResponseDTO;
import com.omarcosallan.flletwise.dto.user.UserMinDTO;
import com.omarcosallan.flletwise.mappers.ResponseWrapper;
import com.omarcosallan.flletwise.services.AuthService;
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
    public ResponseEntity<ResponseWrapper<UserMinDTO>> getProfile() {
        ResponseWrapper<UserMinDTO> result = authService.getProfile();
        return ResponseEntity.ok(result);
    }
}
