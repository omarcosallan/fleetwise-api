package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.vehicle.CreateVehicleDTO;
import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getVehicles(@PathVariable("slug") String slug) {
        List<VehicleDTO> result = vehicleService.getVehicles(slug);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'create', 'Vehicle')")
    public ResponseEntity<ResponseWrapper<UUID>> createVehicle(@PathVariable("slug") String slug, @RequestBody CreateVehicleDTO body) {
        UUID vehicleId = vehicleService.createVehicle(slug, body);
        return ResponseEntity.ok(new ResponseWrapper<>("vehicleId", vehicleId));
    }
}
