package com.modive.adminservice.domain.event.service;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.domain.event.dto.EventsByDriveDTO;

import java.util.List;

public interface EventService {
    List<EventsByDriveDTO> getTotalEventCntByType(Long driveId);
    List<TotalEventCntByReasonItem> getTotalEventCntByType();
}
