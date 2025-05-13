package com.modive.adminservice.event.service.impl;

import com.modive.adminservice.event.dto.EventsByDriveDTO;
import com.modive.adminservice.event.repository.EventRepository;
import com.modive.adminservice.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<EventsByDriveDTO> getEventCntByType(Long driveId) {
        return eventRepository.countByTypeGroupedByDriveId(driveId);
    }
}
