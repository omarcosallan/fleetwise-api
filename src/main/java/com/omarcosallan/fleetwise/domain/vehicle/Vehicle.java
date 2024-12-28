package com.omarcosallan.fleetwise.domain.vehicle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String model;
    private String manufacturer;

    @Column(unique = true, nullable = false)
    private String plate;

    @Column(unique = true, nullable = false)
    private String register;

    private Integer manufacturingYear;
    private Boolean rented;
    private Boolean active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Organization owner;
}
