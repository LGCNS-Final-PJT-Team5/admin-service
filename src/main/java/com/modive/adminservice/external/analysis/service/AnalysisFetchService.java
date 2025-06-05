package com.modive.adminservice.external.analysis.service;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;

import java.util.List;
import java.util.Map;

public interface AnalysisFetchService {
    List<EventsByDriveDTO> getTotalEventCntByType(String driveId);
    List<TotalEventCntByReasonItem> getTotalEventCntByType();
    Map<String, List<EventsByDriveDTO>> getTotalEventCntByType(List<String> driveIds);
}
