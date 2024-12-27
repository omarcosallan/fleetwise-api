package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    public Member getMember(String slug) {
        return memberRepository.findByUserIdAndOrganizationSlug(authService.authenticated().getId(), slug)
                .orElseThrow(() -> new UnauthorizedException("You're not a member of this organization."));
    }

    public Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId) {
        return memberRepository.findByUserIdAndOrganizationId(transferToUserId, organizationId);
    }
}
