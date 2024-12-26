package com.omarcosallan.flletwise.repositories;

import com.omarcosallan.flletwise.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUserIdAndOrganizationSlug(UUID userId, String slug);
}
