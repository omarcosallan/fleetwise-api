package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.invite.Invite;
import com.omarcosallan.fleetwise.projections.InviteProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    @Query("""
                SELECT i.id AS id, i.role AS role, i.email AS email, i.createdAt AS createdAt,
                       o.name as organizationName, u.id AS userId, u.name AS userName, u.avatarUrl as userAvatarUrl
                FROM Invite i
                JOIN i.organization o
                JOIN i.author u
                WHERE i.organization.id = :organizationId
                ORDER BY i.createdAt DESC
            """)
    List<InviteProjection> findByOrganizationIdOrderByCreatedAtDesc(@Param("organizationId") UUID organizationId);

    Optional<Invite> findByEmailAndOrganizationId(String email, UUID id);

    Optional<Invite> findByIdAndOrganizationId(UUID inviteId, UUID id);

    @Query("""
                SELECT i.id AS id, i.role AS role, i.email AS email, i.createdAt AS createdAt, o.name as organizationName, u.id AS userId, u.name AS userName, u.avatarUrl as userAvatarUrl
                FROM Invite i
                JOIN Organization o ON o.id = i.organization.id
                JOIN User u ON u.id = i.author.id
                WHERE i.email = :email
            """)
    List<InviteProjection> findPendingInvitesByEmail(@Param("email") String email);
}
