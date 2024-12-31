package com.omarcosallan.fleetwise.services;

import com.omarcosallan.fleetwise.dto.organization.ActivityDTO;
import com.omarcosallan.fleetwise.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<ActivityDTO> getRecentActivities() {
        return activityRepository.findRecentActivities(AuthService.authenticated().getId())
                .stream()
                .map(activity -> new ActivityDTO(
                        activity.getType(),
                        activity.getCreatedAt(),
                        activity.getOwnerName(),
                        activity.getOrganizationName()
                ))
                .toList();
    }
}
