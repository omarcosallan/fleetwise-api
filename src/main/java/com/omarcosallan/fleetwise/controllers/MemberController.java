package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.member.MemberDTO;
import com.omarcosallan.fleetwise.dto.member.UpdateMemberRequestDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'get', 'Member')")
    public ResponseEntity<List<MemberDTO>> getMembers(@PathVariable("slug") String slug) {
        List<MemberDTO> result = memberService.getMembers(slug);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{memberId}")
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'delete', 'Member')")
    public ResponseEntity<Void> removeMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId) {
        memberService.removeMember(slug, memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{memberId}")
    @PreAuthorize("@authorizationService.hasPermission(#slug, 'update', 'Member')")
    public ResponseEntity<Void> updateMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId, @RequestBody UpdateMemberRequestDTO body) {
        memberService.updateMember(slug, memberId,body);
        return ResponseEntity.noContent().build();
    }
}
