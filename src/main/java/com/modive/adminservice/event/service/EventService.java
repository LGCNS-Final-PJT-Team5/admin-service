package com.modive.adminservice.event.service;

import com.modive.adminservice.event.dto.EventsByDriveDTO;

import java.util.List;

public interface EventService {
    List<EventsByDriveDTO> getEventCntByType(Long driveId);
}
