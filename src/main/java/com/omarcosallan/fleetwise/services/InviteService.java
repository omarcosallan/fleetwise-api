package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class InviteService {
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

        Optional<Invite> inviteWithSameEmail = inviteRepository.findByEmailAndOrganizationId(email, org.getId());

        if (inviteWithSameEmail.isPresent()) {
            throw new BadRequestException("A member with this e-mail already belongs to your organization.");
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
        return new ResponseWrapper<>("invite", InviteMapper.INSTANCE.toMemberDTO(invite));
    }
}
