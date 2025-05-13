package com.modive.adminservice.event.service;

import com.modive.adminservice.dashboardadmin.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.event.dto.EventsByDriveDTO;

import java.util.List;

public interface EventService {
    List<EventsByDriveDTO> getTotalEventCntByType(Long driveId);
    List<TotalEventCntByReasonItem> getTotalEventCntByType();
}
