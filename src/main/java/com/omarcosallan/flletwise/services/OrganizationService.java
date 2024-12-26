package com.omarcosallan.flletwise.services;

import com.omarcosallan.flletwise.domain.enums.Role;
import com.omarcosallan.flletwise.domain.member.Member;
import com.omarcosallan.flletwise.domain.organization.Organization;
import com.omarcosallan.flletwise.domain.user.User;
import com.omarcosallan.flletwise.dto.organization.CreateOrganizationRequestDTO;
import com.omarcosallan.flletwise.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.flletwise.mappers.ResponseWrapper;
import com.omarcosallan.flletwise.repositories.OrganizationRepository;
import com.omarcosallan.flletwise.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationRepository organizationRepository;

    public ResponseWrapper<UUID> createOrganization(CreateOrganizationRequestDTO body) {
        User currentUser = userService.authenticated();

        if (body.domain() != null) {
            Optional<Organization> organizationByDomain = organizationRepository.findByDomain(body.domain());

            if (organizationByDomain.isPresent()) {
                throw new OrganizationDomainAlreadyExistsException();
            }
        }

        Organization organization = new Organization();

        organization.setName(body.name());
        organization.setSlug(SlugUtils.createSlug(body.name()));
        organization.setDomain(body.domain());
        organization.setShouldAttachUsersByDomain(body.shouldAttachUsersByDomain());
        organization.setOwner(currentUser);

        Member member = new Member();
        member.setUser(currentUser);
        member.setRole(Role.ADMIN);
        organization.addMember(member);

        organizationRepository.save(organization);

        return new ResponseWrapper<UUID>("organizationId", organization.getId());
    }
}
