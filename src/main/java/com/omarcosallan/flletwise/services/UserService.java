package com.omarcosallan.flletwise.services;

import com.omarcosallan.flletwise.domain.user.User;
import com.omarcosallan.flletwise.dto.CreateUserDTO;
import com.omarcosallan.flletwise.exceptions.UserAlreadyExistsException;
import com.omarcosallan.flletwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User create(CreateUserDTO body) {
        Optional<User> userAlreadyExists =  userRepository.findByEmail(body.email());

        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(body.password());

        User user = new User();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setPasswordHash(passwordHash);


        return userRepository.save(user);
    }
}
