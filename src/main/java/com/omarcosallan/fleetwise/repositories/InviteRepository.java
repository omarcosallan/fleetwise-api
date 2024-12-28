package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.invite.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InviteRepository extends JpaRepository<Invite, UUID> {
    List<Invite> findByOrganizationIdOrderByCreatedAtDesc(UUID id);

    Optional<Invite> findByEmailAndOrganizationId(String email, UUID id);
}
