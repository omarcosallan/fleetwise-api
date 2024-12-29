package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.invite.CreateInviteDTO;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.exceptions.BadRequestException;
import com.omarcosallan.fleetwise.exceptions.InviteNotFoundException;
import com.omarcosallan.fleetwise.mappers.InviteMapper;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.repositories.InviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InviteService {
    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private OrganizationService organizationService;

    public List<InviteDTO> getInvites(String slug) {
        OrganizationDTO org = organizationService.getOrganization(slug);

        List<Invite> invites = inviteRepository.findByOrganizationIdOrderByCreatedAtDesc(org.id());

        return invites.stream().map(InviteMapper.INSTANCE::toInviteDTO).collect(Collectors.toList());
    }

    @Transactional
    public ResponseWrapper<UUID> createInvite(String slug, CreateInviteDTO body) {
        String email = body.email();
        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            throw new BadRequestException("Invalid email format.");
        }
        String domain = emailParts[1];

        Organization org = organizationService.getEntityOrganization(slug);

        if (org.isShouldAttachUsersByDomain() &&
                domain.equals(org.getDomain())) {
            throw new BadRequestException("Users with " + domain + " domain will join your organization automatically on login.");
        }

        inviteRepository.findByEmailAndOrganizationId(email, org.getId())
                .ifPresent(invite -> {
                    throw new BadRequestException("A member with this e-mail already belongs to your organization.");
                });

        Member memberAlreadyExists = memberService.findByEmailAndOrganizationId(email, org.getId());
        if (memberAlreadyExists != null) {
            throw new BadRequestException("A user with the email " + email + " already exists in your organization.");
        }

        Invite invite = new Invite();
        invite.setOrganization(org);
        invite.setEmail(email);
        invite.setRole(body.role());
        invite.setAuthor(AuthService.authenticated());

        inviteRepository.save(invite);

        return new ResponseWrapper<>("inviteId", invite.getId());
    }

    public InviteDTO getInvite(UUID inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(InviteNotFoundException::new);
        return InviteMapper.INSTANCE.toInviteDTO(invite);
    }

    @Transactional
    public void acceptInvite(UUID inviteId) {
        User user = AuthService.authenticated();

        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new BadRequestException("Invite not found or expired."));

        if (!invite.getEmail().equals(user.getEmail())) {
            throw new BadRequestException("This invite belongs to another user.");
        }

        memberService.create(user, invite.getOrganization(), invite.getRole());

        inviteRepository.deleteById(invite.getId());
    }

    @Transactional
    public void rejectInvite(UUID inviteId) {
        User user = AuthService.authenticated();

        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new BadRequestException("Invite not found or expired."));

        if (!invite.getEmail().equals(user.getEmail())) {
            throw new BadRequestException("This invite belongs to another user.");
        }

        inviteRepository.deleteById(invite.getId());
    }

    @Transactional
    public void revokeInvite(String slug, UUID inviteId) {
        Organization org = organizationService.getEntityOrganization(slug);

        inviteRepository.findByIdAndOrganizationId(inviteId, org.getId())
                .orElseThrow(InviteNotFoundException::new);

        inviteRepository.deleteById(inviteId);
    }

    public List<InviteDTO> getPendingInvites() {
        User user = AuthService.authenticated();
        List<Invite> invites = inviteRepository.findByEmail(user.getEmail());
        return  invites.stream().map(InviteMapper.INSTANCE::toInviteDTO).collect(Collectors.toList());
    }
}
