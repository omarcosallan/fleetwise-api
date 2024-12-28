package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.repositories.InviteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

        List<InviteDTO> result = invites.stream().map(i -> new InviteDTO(i.getId(), i.getRole(), i.getEmail(), i.getCreatedAt(), new InviteDTO.Author(i.getAuthor()))).collect(Collectors.toList());

        return new ResponseWrapper<>("invites", result);
    }
}
