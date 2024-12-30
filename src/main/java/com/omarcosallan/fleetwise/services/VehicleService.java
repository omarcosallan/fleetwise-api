package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.UpdateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.exceptions.VehicleAlreadyExistsException;
import com.omarcosallan.fleetwise.exceptions.VehicleNotFoundException;
import com.omarcosallan.fleetwise.mappers.VehicleMapper;
import com.omarcosallan.fleetwise.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private MemberService memberService;

    public List<VehicleDTO> getVehicles(String slug) {
        List<Vehicle> result = vehicleRepository.findAllByOrganizationSlugOrderByCreatedAtDesc(slug);
        return result.stream().map(VehicleMapper.INSTANCE::toVehicleDTO).collect(Collectors.toList());
    }

    @Transactional
    public UUID createVehicle(String slug, CreateVehicleDTO body) {
        Member member = memberService.getCurrentMember(slug);

        vehicleRepository.findByPlateOrRegister(body.plate(), body.register())
                .ifPresent(vehicle -> {
                    throw new VehicleAlreadyExistsException();
                });

        Vehicle vehicle = new Vehicle();
        vehicle.setModel(body.model());
        vehicle.setManufacturer(body.manufacturer());
        vehicle.setManufacturingYear(body.manufacturingYear());
        vehicle.setPlate(body.plate());
        vehicle.setRegister(body.register());
        vehicle.setActive(body.active());
        vehicle.setRented(body.rented());
        vehicle.setOwner(member.getUser());
        vehicle.setOrganization(member.getOrganization());

        vehicleRepository.save(vehicle);

        return vehicle.getId();
    }

    public VehicleDTO getVehicle(String slug, UUID vehicleId) {
        Vehicle result = vehicleRepository.findByOrganizationSlugAndId(slug, vehicleId)
                .orElseThrow(VehicleNotFoundException::new);
        return VehicleMapper.INSTANCE.toVehicleDTO(result);
    }

    public void updateVehicle(String slug, String plate, UpdateVehicleDTO body) {
        Vehicle vehicle = vehicleRepository.findByOrganizationSlugAndPlate(slug, plate)
                .orElseThrow(VehicleNotFoundException::new);

        vehicle.setModel(body.model());
        vehicle.setManufacturer(body.manufacturer());
        vehicle.setManufacturingYear(body.manufacturingYear());
        vehicle.setRented(body.rented());
        vehicle.setActive(body.active());

        vehicleRepository.save(vehicle);
    }
}
