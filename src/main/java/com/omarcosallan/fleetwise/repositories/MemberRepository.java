package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.projections.MemberProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUserIdAndOrganizationSlug(UUID userId, String slug);

    Optional<Member> findByUserIdAndOrganizationId(UUID transferToUserId, UUID organizationId);

    @Query("""
            SELECT m.id AS id, m.user.id AS userId, m.role AS role, m.user.name AS userName, m.user.email AS userEmail, m.user.avatarUrl as userAvatarUrl
            FROM Member m
            WHERE m.organization.slug = :slug
            ORDER BY m.role ASC
            """)
    List<MemberProjection> findByOrganizationSlugOrderByRoleAsc(@Param("slug") String slug);

    void deleteByIdAndOrganizationSlug(UUID memberId, String slug);

    Optional<Member> findByIdAndOrganizationSlug(UUID memberId, String slug);

    Optional<Object> findByUserEmailAndOrganizationId(String email, UUID organizationId);
}
