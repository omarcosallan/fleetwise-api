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
    private OrganizationRepository organizationRepository;

    @Autowired
    private MemberService memberService;

    @Transactional
    public ResponseWrapper<UUID> createOrganization(CreateOrganizationRequestDTO body) {
        User currentUser = AuthService.authenticated();

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

    public MembershipDTO getMembership(String slug) {
        Member member = memberService.getCurrentMember(slug);
        return MembershipMapper.INSTANCE.toMembershipDTO(member);
    }

    public Organization getEntityOrganization(String slug) {
        return organizationRepository.findBySlug(slug);
    }

    public OrganizationDTO getOrganization(String slug) {
        Organization org = organizationRepository.findBySlug(slug);
        return OrganizationMapper.INSTANCE.toOrganizationDTO(org);
    }

    public List<OrganizationWithOwnerDTO> getOrganizations() {
        User user = AuthService.authenticated();
        return organizationRepository.findOrganizationsByUserId(user.getId());
    }

    @Transactional
    public void updateOrganization(String slug, UpdateOrganizationDTO body) {
        Organization organization = organizationRepository.findBySlug(slug);

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
        organizationRepository.deleteBySlug(slug);
    }

    @Transactional
    public void transferOrganization(String slug, TransferOrganizationRequestDTO body) {
        Organization organization = organizationRepository.findBySlug(slug);

        Member transferMembership = memberService
                .findByUserIdAndOrganizationId(body.transferToUserId(), organization.getId())
                .orElseThrow(() -> new BadRequestException("You're not a member of this organization."));

        transferMembership.setRole(Role.ADMIN);
        organization.setOwner(transferMembership.getUser());

        organizationRepository.save(organization);
    }
}
