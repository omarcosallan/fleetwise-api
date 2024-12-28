package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.member.MembershipDTO;
import com.omarcosallan.fleetwise.dto.organization.*;
import com.omarcosallan.fleetwise.exceptions.BadRequestException;
import com.omarcosallan.fleetwise.exceptions.OrganizationDomainAlreadyExistsException;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
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
    private AuthService authService;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
    public ResponseWrapper<UUID> createOrganization(CreateOrganizationRequestDTO body) {
        User currentUser = authService.authenticated();

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

    public ResponseWrapper<List<OrganizationWithOwnerDTO>> getOrganizations() {
        User user = authService.authenticated();
        List<OrganizationWithOwnerDTO> orgs = organizationRepository.findOrganizationsByUserId(user.getId());
        return new ResponseWrapper<>("organizations", orgs);
    }

    @Transactional
    public void updateOrganization(String slug, UpdateOrganizationDTO body) {
        Member member = memberService.getMember(slug);

        boolean canUpdateOrganization = member.getRole().equals(Role.ADMIN);
        if (!canUpdateOrganization) {
            throw new UnauthorizedException("You're not allowed to update this organization.");
        }

        Organization organization = member.getOrganization();

        if (body.domain() != null) {
            Optional<Organization> organizationByDomain = organizationRepository.findFirstByDomainAndIdNot(body.domain(), organization.getId());

            if (organizationByDomain.isPresent()) {
                throw new OrganizationDomainAlreadyExistsException();
            }
        }

        organization.setName(body.name());
        organization.setDomain(body.domain());
        organization.setSlug(SlugUtils.createSlug(body.name()));
        organization.setShouldAttachUsersByDomain(body.shouldAttachUsersByDomain());

        organizationRepository.save(organization);
    }

    public Organization findFirstByDomainAndShouldAttachUsersByDomain(String domain) {
        return organizationRepository.findFirstByDomainAndShouldAttachUsersByDomainTrue(domain)
                .orElse(null);
    }

    @Transactional
    public void shutdownOrganization(String slug) {
        Member member = memberService.getMember(slug);

        boolean canDeleteOrganization = member.getRole().equals(Role.ADMIN);
        if (!canDeleteOrganization) {
            throw new UnauthorizedException("You're not allowed to shutdown this organization.");
        }

        organizationRepository.deleteById(member.getOrganization().getId());
    }

    @Transactional
    public void transferOrganization(String slug, TransferOrganizationRequestDTO body) {
        Member member = memberService.getMember(slug);
        Organization organization = member.getOrganization();

        boolean canTransferOwnership = member.getRole() == Role.ADMIN
                && organization.getOwner().equals(member.getUser());
        if (!canTransferOwnership) {
            throw new UnauthorizedException("You're not allowed to transfer this organization ownership.");
        }

        Member transferMembership = memberService
                .findByUserIdAndOrganizationId(body.transferToUserId(), organization.getId())
                .orElseThrow(() -> new BadRequestException("You're not a member of this organization."));

        transferMembership.setRole(Role.ADMIN);
        organization.setOwner(transferMembership.getUser());

        organizationRepository.save(organization);
    }
}
