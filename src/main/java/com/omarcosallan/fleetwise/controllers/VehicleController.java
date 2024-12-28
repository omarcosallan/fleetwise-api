package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.vehicle.VehicleDTO;
import com.omarcosallan.fleetwise.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
