package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.member.MemberDTO;
import com.omarcosallan.fleetwise.dto.member.UpdateMemberRequestDTO;
import com.omarcosallan.fleetwise.exceptions.MemberNotFoundException;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.mappers.MemberMapper;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    public Member getCurrentMember(String slug) {
        return memberRepository.findByUserIdAndOrganizationSlug(authService.authenticated().getId(), slug)
                .orElseThrow(() -> new UnauthorizedException("You're not a member of this organization."));
    }

    public Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId) {
        return memberRepository.findByUserIdAndOrganizationId(transferToUserId, organizationId);
    }

    public List<MemberDTO> getMembers(String slug) {
        Member member = getCurrentMember(slug);

        boolean canGetUser = member.getRole() == Role.MEMBER || member.getRole() == Role.ADMIN;
        if (!canGetUser) {
            throw new UnauthorizedException("You're not allowed to see organization members.");
        }

        List<Member> members = memberRepository.findByOrganizationIdOrderByRoleAsc(member.getOrganization().getId());

        return members.stream().map(MemberMapper.INSTANCE::toMemberDTO).collect(Collectors.toList());
    }

    @Transactional
    public void removeMember(String slug, UUID memberId) {
        Member member = getCurrentMember(slug);

        boolean canDeleteUser = member.getRole() == Role.ADMIN;
        if (!canDeleteUser) {
            throw new UnauthorizedException("You're not allowed to remove this member from organization.");
        }

        memberRepository.deleteByIdAndOrganizationId(memberId, member.getOrganization().getId());
    }

    @Transactional
    public void updateMember(String slug, UUID memberId, UpdateMemberRequestDTO body) {
        Member member = getCurrentMember(slug);

        boolean canUpdateUser = member.getRole() == Role.ADMIN;
        if (!canUpdateUser) {
            throw new UnauthorizedException("You're not allowed to update this member.");
        }

        Member updatingMember = memberRepository.findByIdAndOrganizationId(memberId, member.getOrganization().getId())
                .orElseThrow(MemberNotFoundException::new);

        updatingMember.setRole(body.role());
        memberRepository.save(updatingMember);
    }

    @Transactional
    public Member create(User user, Organization org, Role role) {
        Member member = new Member();
        member.setUser(user);
        member.setOrganization(org);
        member.setRole(role);
        return memberRepository.save(member);
    }

    public Member findByEmailAndOrganizationId(String email, UUID organizationId) {
        return (Member) memberRepository.findByUserEmailAndOrganizationId(email, organizationId)
                .orElse(null);
    }
}
