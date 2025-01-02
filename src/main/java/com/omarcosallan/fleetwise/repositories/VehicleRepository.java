package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.projections.VehicleOrgAndAuthorProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    @Query("""
            SELECT e.id AS id, e.model AS model, e.manufacturer AS manufacturer, e.manufacturingYear AS manufacturingYear, e.plate AS plate, e.register AS register, e.active AS active, e.rented AS rented, e.createdAt AS createdAt, o.id AS organizationId, u.id AS authorId, u.name AS authorName, u.avatarUrl AS authorAvatarUrl
            FROM Vehicle e
            JOIN FETCH Organization o ON e.organization.id = o.id
            JOIN FETCH User u ON e.owner.id = u.id
            WHERE e.organization.slug = :organizationSlug
            AND (:search IS NULL
                OR LOWER(e.plate) LIKE %:search%
                OR e.register LIKE %:search%
                OR LOWER(e.model) LIKE %:search%
                OR LOWER(e.manufacturer) LIKE %:search%
            )
            ORDER BY e.createdAt DESC
            """)
    Page<VehicleOrgAndAuthorProjection> findAllByOrganizationSlugAndFiltered(@Param("organizationSlug") String organizationSlug, @Param("search") String search, Pageable pageable);

    Optional<Vehicle> findFirstByPlateOrRegister(String plate, String register);

    @Query("""
            SELECT e.id AS id, e.model AS model, e.manufacturer AS manufacturer, e.manufacturingYear AS manufacturingYear, e.plate AS plate, e.register AS register, e.active AS active, e.rented AS rented, e.createdAt AS createdAt, o.id AS organizationId, u.id AS authorId, u.name AS authorName, u.avatarUrl AS authorAvatarUrl
            FROM Vehicle e
            JOIN FETCH Organization o ON e.organization.id = o.id
            JOIN FETCH User u ON e.owner.id = u.id
            WHERE e.organization.slug = :organizationSlug AND e.id = :vehicleId
            """)
    Optional<VehicleOrgAndAuthorProjection> findByOrganizationSlugAndId(@Param("organizationSlug") String organizationSlug, @Param("vehicleId") UUID vehicleId);

    Optional<Vehicle> findFirstByOrganizationSlugAndPlate(String slug, String plate);
}
