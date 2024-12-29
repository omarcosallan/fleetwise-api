package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.exceptions.UserNotFoundException;
import com.omarcosallan.fleetwise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Predicate;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean hasPermission(String slug, String action, String entity) {
        Member member = memberService.getCurrentMember(slug);
        Organization organization = member.getOrganization();

        Map<String, Predicate<Member>> specificPermissions = Map.of(
                "ADMIN_Organization_transfer_ownership", m -> organization.getOwner().equals(m.getUser()),
                "ADMIN_Organization_update", m -> organization.getOwner().equals(m.getUser()),
                "MEMBER_Vehicle_create", m -> true,
                "MEMBER_Vehicle_get", m -> true,
                "MEMBER_Vehicle_update", m -> organization.getOwner().equals(m.getUser()),
                "MEMBER_Vehicle_delete", m -> organization.getOwner().equals(m.getUser()),
                "BILLING_Billing_manage", m -> true
        );

        String specificKey = String.format("%s_%s_%s", member.getRole(), entity, action);

        if (specificPermissions.containsKey(specificKey)) {
            return specificPermissions.get(specificKey).test(member);
        }

        return Role.ADMIN.equals(member.getRole());
    }
}
