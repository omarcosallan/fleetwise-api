package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.projections.OrganizationsOwnerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    Optional<Organization> findByDomain(String domain);

    Optional<Organization> findFirstByDomainAndIdNot(String domain, UUID id);

    Optional<Organization> findFirstByDomainAndShouldAttachUsersByDomainTrue(String domain);

    @Query("""
            SELECT o.id AS id, o.name AS name, o.slug AS slug, o.domain AS domain, o.shouldAttachUsersByDomain AS shouldAttachUsersByDomain, o.avatarUrl AS avatarUrl, o.createdAt AS createdAt, m.role AS role, u.id AS ownerId, u.name AS ownerName, u.email AS ownerEmail, u.avatarUrl AS ownerAvatarUrl
            FROM Organization o
            JOIN o.members m
            JOIN o.owner u
            WHERE m.user.id = :userId
            """)
    List<OrganizationsOwnerProjection> findOrganizationsByUserId(@Param("userId") UUID userId);

    Organization findBySlug(String slug);

    void deleteBySlug(String slug);
}
