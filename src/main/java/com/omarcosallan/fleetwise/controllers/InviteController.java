package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @GetMapping(value = "/organizations/{slug}/invites")
    public ResponseEntity<ResponseWrapper<List<InviteDTO>>> getInvites(@PathVariable("slug") String slug) {
        ResponseWrapper<List<InviteDTO>> result = inviteService.getInvites(slug);
        return ResponseEntity.ok(result);
    }
}
