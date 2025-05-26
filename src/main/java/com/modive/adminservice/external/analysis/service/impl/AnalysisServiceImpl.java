package com.modive.adminservice.external.analysis.service.impl;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.external.analysis.client.AnalysisClient;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;
import com.modive.adminservice.external.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisClient analysisClient;

    /**
     * 특정 운전의 이벤트 발생 횟수 계산
     *
     * @param driveId 운전 ID
     * @return 특정 운전의 이벤트 발생 횟수
     */
    @Override
    public List<EventsByDriveDTO> getTotalEventCntByType(Long driveId) {
        return analysisClient.getTotalEventCntByType(driveId);
    }

    /**
     * 전체 누적 이벤트 발생 횟수 계산
     *
     * @return 전체 누적 이벤트 발생 횟수
     */
    @Override
    public List<TotalEventCntByReasonItem> getTotalEventCntByType() {
        return analysisClient.getTotalEventCntByType();
    }
}
