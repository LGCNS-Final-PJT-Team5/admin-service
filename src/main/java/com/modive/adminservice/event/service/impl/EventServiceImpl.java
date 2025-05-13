package com.modive.adminservice.event.service.impl;

import com.modive.adminservice.dashboardadmin.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.event.dto.EventsByDriveDTO;
import com.modive.adminservice.event.repository.EventRepository;
import com.modive.adminservice.event.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    /**
     * 특정 운전의 이벤트별 횟수 계산
     *
     * @param driveId 운전 ID
     * @return 특정 운전의 이벤트별 횟수
     */
    @Override
    public List<EventsByDriveDTO> getTotalEventCntByType(Long driveId) {
        return eventRepository.countByTypeGroupedByDriveId(driveId);
    }

    /**
     * 운전별 누적 이벤트 횟수 계산
     *
     * @return
     */
    @Override
    public List<TotalEventCntByReasonItem> getTotalEventCntByType() {
        return eventRepository.totalCntByType().stream()
                .map(e -> TotalEventCntByReasonItem.builder()
                        .reason(e.getReason())
                        .count(e.getCount())
                        .build())
                .collect(Collectors.toList());
    }
}
