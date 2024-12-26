package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.exceptions.UserNotFoundException;
import com.omarcosallan.fleetwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
