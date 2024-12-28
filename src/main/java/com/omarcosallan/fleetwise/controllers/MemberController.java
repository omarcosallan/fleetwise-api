package com.omarcosallan.fleetwise.controllers;

import com.omarcosallan.fleetwise.dto.member.MemberDTO;
import com.omarcosallan.fleetwise.dto.member.UpdateMemberRequestDTO;
import com.omarcosallan.fleetwise.mappers.ResponseWrapper;
import com.omarcosallan.fleetwise.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/organizations/{slug}/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<MemberDTO>>> getMembers(@PathVariable("slug") String slug) {
        ResponseWrapper<List<MemberDTO>> result = memberService.getMembers(slug);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping(value = "/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId) {
        memberService.removeMember(slug, memberId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{memberId}")
    public ResponseEntity<Void> updateMember(@PathVariable("slug") String slug, @PathVariable("memberId") UUID memberId, @RequestBody UpdateMemberRequestDTO body) {
        memberService.updateMember(slug, memberId,body);
        return ResponseEntity.noContent().build();
    }
}
