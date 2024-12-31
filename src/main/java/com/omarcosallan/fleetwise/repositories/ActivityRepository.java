package com.omarcosallan.fleetwise.repositories;

import com.omarcosallan.fleetwise.domain.organization.Organization;
import com.omarcosallan.fleetwise.dto.organization.ActivityDTO;
import com.omarcosallan.fleetwise.projections.RecentActivityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Organization, UUID> {
    @Query(nativeQuery = true, value = """
            SELECT
                'organization' AS type,
                o.created_at AS createdAt,
                u.name AS ownerName,
                o.name AS organizationName
            FROM organizations o
            JOIN users u ON o.owner_id = u.id
            JOIN members m ON m.user_id = u.id
            WHERE m.organization_id = o.id
            
            UNION ALL
            
            SELECT
                'vehicle' AS type,
                v.created_at AS createdAt,
                u.name AS ownerName,
                org.name AS organizationName
            FROM vehicles v
            JOIN users u ON v.owner_id = u.id
            LEFT JOIN organizations org ON v.organization_id = org.id
            JOIN members m ON m.user_id = u.id
            WHERE m.organization_id = org.id
            
            UNION ALL
            
            SELECT
                'invite' AS type,
                i.created_at AS createdAt,
                u.name AS ownerName,
                org.name AS organizationName
            FROM invites i
            JOIN users u ON i.author_id = u.id
            LEFT JOIN organizations org ON i.organization_id = org.id
            JOIN members m ON m.user_id = u.id
            WHERE m.organization_id = org.id
            
            ORDER BY createdAt DESC
                        
            LIMIT 4
            """)
    List<RecentActivityProjection> findRecentActivities(@Param("userId") UUID userId);
}
