package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.member.MembershipDTO;
import com.omarcosallan.fleetwise.dto.organization.CreateOrganizationRequestDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationMinDTO;
import com.omarcosallan.fleetwise.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.fleetwise.mappers.MembershipMapper;
import com.omarcosallan.fleetwise.mappers.OrganizationMapper;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.repositories.OrganizationRepository;
import com.omarcosallan.fleetwise.utils.SlugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {
    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
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

    public ResponseWrapper<MembershipDTO> getMembership(String slug) {
        Member member = memberService.getMember(slug);
        MembershipDTO membershipDTO = MembershipMapper.INSTANCE.toMembershipDTO(member);
        return new ResponseWrapper<>("membership", membershipDTO);
    }

    public ResponseWrapper<OrganizationDTO> getOrganization(String slug) {
        Member member = memberService.getMember(slug);
        OrganizationDTO org = OrganizationMapper.INSTANCE.toOrganizationDTO(member.getOrganization());
        return new ResponseWrapper<>("organization", org);
    }

    public ResponseWrapper<List<OrganizationMinDTO>> getOrganizations() {
        User user = userService.authenticated();

        List<OrganizationMinDTO> orgs = organizationRepository.findOrganizationsByUserId(user.getId());
        return new ResponseWrapper<List<OrganizationMinDTO>>("organizations", orgs);
    }
}
