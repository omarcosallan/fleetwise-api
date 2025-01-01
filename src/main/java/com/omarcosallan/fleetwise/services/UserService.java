package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.user.CreateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UpdateUserDTO;
import com.omarcosallan.fleetwise.dto.user.UserMinDTO;
import com.omarcosallan.fleetwise.exceptions.UserAlreadyExistsException;
import com.omarcosallan.fleetwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private OrganizationService organizationService;

    @Transactional
    public UserMinDTO create(CreateUserDTO body) {
        userRepository.findByEmail(body.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

        String passwordHash = passwordEncoder.encode(body.password());

        String[] emailParts = body.email().split("@");
        String domain = emailParts[1];

        Organization autoJoinOrganization = organizationService
                .findFirstByDomainAndShouldAttachUsersByDomain(domain);

        User user = new User();
        user.setName(body.name());
        user.setEmail(body.email());
        user.setPasswordHash(passwordHash);

        if (autoJoinOrganization != null) {
            Member member = new Member();
            member.setUser(user);
            member.setOrganization(autoJoinOrganization);
            user.getMemberOn().add(member);
        }

        userRepository.save(user);

        return new UserMinDTO(user.getId(), user.getName(), user.getEmail(), user.getAvatarUrl());
    }

    public void update(UpdateUserDTO body) {
        User user = AuthService.authenticated();

        if (body.email() != null && !body.email().equals(user.getEmail())) {
            user.setEmail(body.email());
        }

        if (body.name() != null && !body.name().equals(user.getName())) {
            user.setName(body.name());
        }

        userRepository.save(user);
    }
}
