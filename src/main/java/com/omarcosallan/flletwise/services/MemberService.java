package com.omarcosallan.flletwise.services;

import com.omarcosallan.flletwise.domain.member.Member;
import com.omarcosallan.flletwise.exceptions.UnauthorizedException;
import com.omarcosallan.flletwise.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserService userService;

    public Member getMember(String slug) {
        return memberRepository.findByUserIdAndOrganizationSlug(userService.authenticated().getId(), slug)
                .orElseThrow(() -> new UnauthorizedException("You're not a member of this organization."));
    }
}
