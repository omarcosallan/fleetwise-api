package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.dto.organization.OrganizationMinDTO;
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

    @Query("SELECT new com.omarcosallan.fleetwise.dto.organization.OrganizationMinDTO(o.id, o.name, o.slug, o.avatarUrl, m.role) FROM Organization o JOIN o.members m WHERE m.user.id = :userId")
    List<OrganizationMinDTO> findOrganizationsByUserId(@Param("userId") UUID userId);
}
