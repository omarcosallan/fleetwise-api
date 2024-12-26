package com.omarcosallan.flletwise.services;

import com.omarcosallan.flletwise.domain.user.User;
import com.omarcosallan.flletwise.dto.user.LoginResponseDTO;
import com.omarcosallan.flletwise.dto.user.UserMinDTO;
import com.omarcosallan.flletwise.dto.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    public LoginResponseDTO login(String email, String password) {
        UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(usernamePassword);

        String token = tokenService.generateToken((User) auth.getPrincipal());

        return new LoginResponseDTO(token, "7d");
    }

    public UserResponseDTO getProfile() {
        User user = userService.authenticated();
        return new UserResponseDTO(new UserMinDTO(user.getId(), user.getName(), user.getEmail(), user.getAvatarUrl()));
    }
}
