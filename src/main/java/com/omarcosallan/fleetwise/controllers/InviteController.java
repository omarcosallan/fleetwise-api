package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.invite.CreateInviteDTO;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/invites")
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<InviteDTO>>> getInvites(@PathVariable("slug") String slug) {
        ResponseWrapper<List<InviteDTO>> result = inviteService.getInvites(slug);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper<UUID>> createInvite(@PathVariable String slug, @RequestBody CreateInviteDTO body) {
        ResponseWrapper<UUID> result = inviteService.createInvite(slug, body);
        return ResponseEntity.ok(result);
    }
}
