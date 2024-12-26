package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.user.CreateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.exceptions.UserAlreadyExistsException;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.mappers.UserMinMapper;
import com.omarcosallan.fleetwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseWrapper<UserMinDTO> create(CreateUserDTO body) {
        Optional<User> userAlreadyExists = userRepository.findByEmail(body.email());

        if (userAlreadyExists.isPresent()) {
            throw new UserAlreadyExistsException();
        }

        String passwordHash = passwordEncoder.encode(body.password());

        User user = new User();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setPasswordHash(passwordHash);

        UserMinDTO userMinDTO = UserMinMapper.INSTANCE.toUserMinDTO(user);
        return new ResponseWrapper<>("user", userMinDTO);
    }

    public User authenticated() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
