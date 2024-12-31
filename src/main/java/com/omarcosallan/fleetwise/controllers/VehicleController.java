package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.GetVehicleResponse;
import com.omarcosallan.fleetwise.dto.vehicle.UpdateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleResponseDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<GetVehicleResponse> getVehicles(@PathVariable("slug") String slug,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(defaultValue = "0") int pageIndex,
                                                          @RequestParam(required = false) String search) {
        GetVehicleResponse result = vehicleService.getVehicles(slug, pageSize, pageIndex, search);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'create', 'Vehicle')")
    public ResponseEntity<ResponseWrapper<UUID>> createVehicle(@PathVariable("slug") String slug, @RequestBody CreateVehicleDTO body) {
        UUID vehicleId = vehicleService.createVehicle(slug, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper<>("vehicleId", vehicleId));
    }

    @GetMapping(value = "/{vehicleId}")
    public ResponseEntity<VehicleResponseDTO> getVehicle(@PathVariable("slug") String slug, @PathVariable("vehicleId") UUID vehicleId) {
        VehicleResponseDTO result = vehicleService.getVehicle(slug, vehicleId);
        return ResponseEntity.ok(result);
    }

    @PutMapping(value = "/{plate}")
    public ResponseEntity<Void> updateVehicle(@PathVariable("slug") String slug, @PathVariable("plate") String plate, @RequestBody UpdateVehicleDTO body) {
        vehicleService.updateVehicle(slug, plate, body);
        return ResponseEntity.noContent().build();
    }
}
