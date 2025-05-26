package com.modive.adminservice.external.analysis.service;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;

import java.util.List;

public interface AnalysisService {
    List<EventsByDriveDTO> getTotalEventCntByType(Long driveId);
    List<TotalEventCntByReasonItem> getTotalEventCntByType();
}
