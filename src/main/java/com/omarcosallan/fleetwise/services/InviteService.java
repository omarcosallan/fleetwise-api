package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.invite.CreateInviteDTO;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.exceptions.BadRequestException;
import com.omarcosallan.fleetwise.exceptions.InviteNotFoundException;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
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
    private AuthService authService;

    @Autowired
    private InviteRepository inviteRepository;

    @Autowired
    private MemberService memberService;

    public ResponseWrapper<List<InviteDTO>> getInvites(String slug) {
        Member member = memberService.getMember(slug);

        boolean canGetInvite = member.getRole() == Role.ADMIN;
        if (!canGetInvite) {
            throw new UnauthorizedException("You're not allowed to get organization invites.");
        }

        List<Invite> invites = inviteRepository.findByOrganizationIdOrderByCreatedAtDesc(member.getOrganization().getId());

        List<InviteDTO> result = invites.stream().map(i -> new InviteDTO(i.getId(), i.getRole(), i.getEmail(), i.getCreatedAt(), new InviteDTO.OrganizationName(i.getOrganization().getName()), new InviteDTO.Author(i.getAuthor()))).collect(Collectors.toList());

        return new ResponseWrapper<>("invites", result);
    }

    @Transactional
    public ResponseWrapper<UUID> createInvite(String slug, CreateInviteDTO body) {
        Member member = memberService.getMember(slug);

        boolean canCreateInvite = member.getRole() == Role.ADMIN;
        if (!canCreateInvite) {
            throw new UnauthorizedException("You're not allowed to create new invites.");
        }

        String email = body.email();
        String[] emailParts = email.split("@");
        if (emailParts.length != 2) {
            throw new BadRequestException("Invalid email format.");
        }
        String domain = emailParts[1];

        Organization org = member.getOrganization();

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
        invite.setAuthor(member.getUser());

        inviteRepository.save(invite);

        return new ResponseWrapper<>("inviteId", invite.getId());
    }

    public ResponseWrapper<InviteDTO> getInvite(UUID inviteId) {
        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(InviteNotFoundException::new);
        return new ResponseWrapper<>("invite", InviteMapper.INSTANCE.toInviteDTO(invite));
    }

    @Transactional
    public void acceptInvite(UUID inviteId) {
        User user = authService.authenticated();

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
        User user = authService.authenticated();

        Invite invite = inviteRepository.findById(inviteId)
                .orElseThrow(() -> new BadRequestException("Invite not found or expired."));

        if (!invite.getEmail().equals(user.getEmail())) {
            throw new BadRequestException("This invite belongs to another user.");
        }

        inviteRepository.deleteById(invite.getId());
    }

    @Transactional
    public void revokeInvite(String slug, UUID inviteId) {
        Member member = memberService.getMember(slug);

        boolean canDeleteInvite = member.getRole() == Role.ADMIN;
        if (!canDeleteInvite) {
            throw new UnauthorizedException("You're not allowed to delete an invite.");
        }

        inviteRepository.findByIdAndOrganizationId(inviteId, member.getOrganization().getId())
                .orElseThrow(InviteNotFoundException::new);

        inviteRepository.deleteById(inviteId);
    }

    public ResponseWrapper<List<InviteDTO>> getPendingInvites() {
        User user = authService.authenticated();

        List<Invite> invites = inviteRepository.findByEmail(user.getEmail());

        return new ResponseWrapper<>("invites", invites.stream().map(InviteMapper.INSTANCE::toInviteDTO).collect(Collectors.toList()));
    }
}
