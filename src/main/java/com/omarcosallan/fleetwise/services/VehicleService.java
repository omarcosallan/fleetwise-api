package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import com.omarcosallan.fleetwise.mappers.VehicleMapper;
import com.omarcosallan.fleetwise.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<VehicleDTO> getVehicles(String slug) {
        List<Vehicle> result = vehicleRepository.findAllByOwnerSlug(slug);
        return result.stream().map(VehicleMapper.INSTANCE::toVehicleDTO).collect(Collectors.toList());
    }
}
