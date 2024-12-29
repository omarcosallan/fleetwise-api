package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.dto.member.MemberDTO;
import com.omarcosallan.fleetwise.dto.member.UpdateMemberRequestDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.exceptions.MemberNotFoundException;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.mappers.MemberMapper;
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

    public Member getCurrentMember(String slug) {
        return memberRepository.findByUserIdAndOrganizationSlug(AuthService.authenticated().getId(), slug)
                .orElseThrow(() -> new UnauthorizedException("You're not a member of this organization."));
    }

    public Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId) {
        return memberRepository.findByUserIdAndOrganizationId(transferToUserId, organizationId);
    }

    public List<MemberDTO> getMembers(String slug) {
        List<Member> members = memberRepository.findByOrganizationSlugOrderByRoleAsc(slug);
        return members.stream().map(MemberMapper.INSTANCE::toMemberDTO).collect(Collectors.toList());
    }

    @Transactional
    public void removeMember(String slug, UUID memberId) {
        memberRepository.deleteByIdAndOrganizationSlug(memberId, slug);
    }

    @Transactional
    public void updateMember(String slug, UUID memberId, UpdateMemberRequestDTO body) {
        Member updatingMember = memberRepository.findByIdAndOrganizationSlug(memberId, slug)
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
