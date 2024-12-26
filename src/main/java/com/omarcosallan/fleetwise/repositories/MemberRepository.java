package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUserIdAndOrganizationSlug(UUID userId, String slug);
}
