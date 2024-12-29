package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.invite.CreateInviteDTO;
import com.omarcosallan.fleetwise.dto.invite.InviteDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.InviteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class InviteController {
    @Autowired
    private InviteService inviteService;

    @GetMapping(value = "/organizations/{slug}/invites")
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'get', 'Invite')")
    public ResponseEntity<List<InviteDTO>> getInvites(@PathVariable("slug") String slug) {
        List<InviteDTO> result = inviteService.getInvites(slug);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/organizations/{slug}/invites")
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'create', 'Invite')")
    public ResponseEntity<ResponseWrapper<UUID>> createInvite(@PathVariable String slug, @RequestBody CreateInviteDTO body) {
        ResponseWrapper<UUID> result = inviteService.createInvite(slug, body);
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "/invites/{inviteId}")
    public ResponseEntity<InviteDTO> getInvite(@PathVariable("inviteId") UUID inviteId) {
        InviteDTO result = inviteService.getInvite(inviteId);
        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/invites/{inviteId}/accept")
    public ResponseEntity<Void> acceptInvite(@PathVariable("inviteId") UUID inviteId) {
        inviteService.acceptInvite(inviteId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/invites/{inviteId}/reject")
    public ResponseEntity<Void> rejectInvite(@PathVariable("inviteId") UUID inviteId) {
        inviteService.rejectInvite(inviteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/organizations/{slug}/invites/{inviteId}")
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'delete', 'Invite')")
    public ResponseEntity<Void> revokeInvite(@PathVariable("slug") String slug, @PathVariable("inviteId") UUID inviteId) {
        inviteService.revokeInvite(slug, inviteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/pending-invites")
    public ResponseEntity<List<InviteDTO>> getPendingInvites() {
        List<InviteDTO> result = inviteService.getPendingInvites();
        return ResponseEntity.ok(result);
    }
}
