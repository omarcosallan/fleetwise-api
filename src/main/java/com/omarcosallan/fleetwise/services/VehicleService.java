package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.enums.Role;
import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import com.omarcosallan.fleetwise.exceptions.UnauthorizedException;
import com.omarcosallan.fleetwise.exceptions.VehicleAlreadyExistsException;
import com.omarcosallan.fleetwise.mappers.VehicleMapper;
import com.omarcosallan.fleetwise.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        List<Vehicle> result = vehicleRepository.findAllByOwnerSlugOrderByCreatedAtDesc(slug);
        return result.stream().map(VehicleMapper.INSTANCE::toVehicleDTO).collect(Collectors.toList());
    }

    @Transactional
    public UUID createVehicle(String slug, CreateVehicleDTO body) {
        Member member = memberService.getCurrentMember(slug);

//        boolean canCreateVehicle = member.getRole().equals(Role.ADMIN);
//        if (!canCreateVehicle) {
//            throw new UnauthorizedException("You're not allowed to create new vehicles.");
//        }

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
        vehicle.setCreatedBy(member.getUser());
        vehicle.setOwner(member.getOrganization());

        vehicleRepository.save(vehicle);

        return vehicle.getId();
    }
}
