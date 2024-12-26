package com.omarcosallan.flletwise.dto.organization;

public record CreateOrganizationRequestDTO(String name, String domain, boolean shouldAttachUsersByDomain) {
}
