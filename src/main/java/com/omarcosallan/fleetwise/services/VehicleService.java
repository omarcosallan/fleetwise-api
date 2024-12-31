package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.domain.member.Member;
import com.omarcosallan.fleetwise.domain.vehicle.Vehicle;
import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.GetVehicleResponse;
import com.omarcosallan.fleetwise.dto.vehicle.UpdateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleResponseDTO;
import com.omarcosallan.fleetwise.exceptions.VehicleAlreadyExistsException;
import com.omarcosallan.fleetwise.exceptions.VehicleNotFoundException;
import com.omarcosallan.fleetwise.mappers.VehicleMapper;
import com.omarcosallan.fleetwise.projections.VehicleOrgAndAuthorProjection;
import com.omarcosallan.fleetwise.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private VehicleMapper mapper;

    public GetVehicleResponse getVehicles(String slug, int pageSize, int pageIndex, String search) {
        Pageable pageable = PageRequest.of(pageIndex, pageSize, Sort.Direction.DESC, "createdAt");
        Page<VehicleOrgAndAuthorProjection> pages = vehicleRepository.findAllByOrganizationSlugAndFiltered(slug, search != null ? search.toLowerCase() : null, pageable);

        List<VehicleResponseDTO> vehicleResponseDTOS = pages.getContent().stream()
                .map(vehicle -> new VehicleResponseDTO(
                        vehicle.getId(),
                        vehicle.getModel(),
                        vehicle.getManufacturer(),
                        vehicle.getManufacturingYear(),
                        vehicle.getPlate(),
                        vehicle.getRegister(),
                        vehicle.getActive(),
                        vehicle.getRented(),
                        vehicle.getCreatedAt(),
                        vehicle.getOrganizationId(),
                        new VehicleResponseDTO.Author(vehicle.getAuthorId(), vehicle.getAuthorName(), vehicle.getAuthorAvatarUrl())
                ))
                .collect(Collectors.toList());

        return new GetVehicleResponse(vehicleResponseDTOS, pageSize, pageIndex, pages.getTotalPages());
    }

    @Transactional
    public UUID createVehicle(String slug, CreateVehicleDTO body) {
        Member member = memberService.getCurrentMember(slug);

        vehicleRepository.findByPlateOrRegister(body.plate(), body.register()).ifPresent(vehicle -> {
            throw new VehicleAlreadyExistsException();
        });

        Vehicle vehicle = mapper.toEntity(body, member.getUser(), member.getOrganization());

        vehicleRepository.save(vehicle);

        return vehicle.getId();
    }

    public VehicleResponseDTO getVehicle(String slug, UUID vehicleId) {
        VehicleOrgAndAuthorProjection result = vehicleRepository.findByOrganizationSlugAndId(slug, vehicleId).orElseThrow(VehicleNotFoundException::new);

        return new VehicleResponseDTO(
                result.getId(),
                result.getModel(),
                result.getManufacturer(),
                result.getManufacturingYear(),
                result.getPlate(),
                result.getRegister(),
                result.getActive(),
                result.getRented(),
                result.getCreatedAt(),
                result.getOrganizationId(),
                new VehicleResponseDTO.Author(result.getAuthorId(), result.getAuthorName(), result.getAuthorAvatarUrl())
        );
    }

    public void updateVehicle(String slug, String plate, UpdateVehicleDTO body) {
        Vehicle vehicle = vehicleRepository.findByOrganizationSlugAndPlate(slug, plate).orElseThrow(VehicleNotFoundException::new);

        vehicle.setModel(body.model());
        vehicle.setManufacturer(body.manufacturer());
        vehicle.setManufacturingYear(body.manufacturingYear());
        vehicle.setRented(body.rented());
        vehicle.setActive(body.active());

        vehicleRepository.save(vehicle);
    }
}
