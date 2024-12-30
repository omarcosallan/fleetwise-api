package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    List<Vehicle> findAllByOrganizationSlugOrderByCreatedAtDesc(String slug);

    Optional<Vehicle> findByPlateOrRegister(String plate, String register);

    Optional<Vehicle> findByOrganizationSlugAndId(String slug, UUID vehicleId);

    Optional<Vehicle> findByOrganizationSlugAndPlate(String slug, String plate);
}
