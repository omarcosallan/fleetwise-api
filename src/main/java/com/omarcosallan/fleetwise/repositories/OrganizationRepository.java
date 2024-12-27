package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.dto.organization.OrganizationDTO;
import com.omarcosallan.fleetwise.dto.organization.OrganizationMinDTO;
import com.omarcosallan.fleetwise.projections.OrganizationProjection;
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
                SELECT 
                    o.id AS id, 
                    o.name AS name, 
                    o.slug AS slug, 
                    o.domain AS domain, 
                    o.shouldAttachUsersByDomain AS shouldAttachUsersByDomain, 
                    o.avatarUrl AS avatarUrl, 
                    o.createdAt AS createdAt, 
                    o.updatedAt AS updatedAt, 
                    m.role AS role, 
                    o.owner.id AS ownerId, 
                    o.owner.name AS ownerName, 
                    o.owner.email AS ownerEmail, 
                    o.owner.avatarUrl AS ownerAvatarUrl
                FROM Organization o 
                JOIN o.members m 
                WHERE m.user.id = :userId
            """)
    List<OrganizationProjection> findOrganizationsByUserId(@Param("userId") UUID userId);
}
