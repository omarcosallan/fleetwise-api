package com.omarcosallan.fleetwise.dto.invite;

import com.omarcosallan.fleetwise.domain.enums.Role;

public record CreateInviteDTO(String email,
                              Role role) {
}
