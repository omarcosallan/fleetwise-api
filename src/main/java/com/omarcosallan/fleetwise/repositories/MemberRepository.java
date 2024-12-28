package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUserIdAndOrganizationSlug(UUID userId, String slug);

    Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId);

    List<Member> findByOrganizationIdOrderByRoleAsc(UUID id);

    void deleteByIdAndOrganizationId(UUID memberId, UUID id);

    Optional<Member> findByIdAndOrganizationId(UUID memberId, UUID id);
}
