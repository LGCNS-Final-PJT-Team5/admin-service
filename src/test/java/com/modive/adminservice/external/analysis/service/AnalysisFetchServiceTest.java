package com.modive.adminservice.external.analysis.service;

import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.external.analysis.client.AnalysisClient;
import com.modive.adminservice.external.analysis.dto.EventTotalCntByTypeDTO;
import com.modive.adminservice.external.analysis.dto.EventsByDriveDTO;
import com.modive.adminservice.external.analysis.service.impl.AnalysisFetchServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnalysisFetchServiceTest {
    @Mock
    private AnalysisClient analysisClient;

    @InjectMocks
    private AnalysisFetchServiceImpl analysisFetchService;

    /**
     * getTotalEventCntByType(String driveId) 테스트 - 단일 운전의 이벤트 발생 횟수 조회
     * 시나리오: 단일 운전 ID로 해당 운전의 이벤트 발생 횟수를 조회하는 경우
     * 기대:
     *  - AnalysisClient의 getTotalEventCntByType 메서드가 정확한 파라미터로 1회 호출됨
     *  - 클라이언트로부터 받은 응답이 그대로 반환됨
     */
    @Test
    @DisplayName("단일 운전의 이벤트 발생 횟수 조회")
    void getTotalEventCntByType_withSingleDriveId_shouldReturnEvents() {
        // given
        String testDriveId = "drive-123";
        List<EventsByDriveDTO> expectedEvents = Arrays.asList(
                createEventsByDriveDTO("SPEEDING", 5),
                createEventsByDriveDTO("SUDDEN_ACCELERATION", 3)
        );

        when(analysisClient.getTotalEventCntByType(testDriveId)).thenReturn(expectedEvents);

        // when
        List<EventsByDriveDTO> result = analysisFetchService.getTotalEventCntByType(testDriveId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedEvents, result);
        verify(analysisClient, times(1)).getTotalEventCntByType(testDriveId);
    }

    /**
     * getTotalEventCntByType() 테스트 - 전체 누적 이벤트 발생 횟수 조회
     * 시나리오: 파라미터 없이 전체 누적 이벤트 발생 횟수를 조회하는 경우
     * 기대:
     *  - AnalysisClient의 파라미터 없는 getTotalEventCntByType 메서드가 1회 호출됨
     *  - 클라이언트로부터 받은 응답이 그대로 반환됨
     */
    @Test
    @DisplayName("전체 누적 이벤트 발생 횟수 조회")
    void getTotalEventCntByType_withNoParam_shouldReturnAllEvents() {
        // given
        List<TotalEventCntByReasonItem> expectedEvents = Arrays.asList(
                createTotalEventCntByReasonItem("SPEEDING", 100),
                createTotalEventCntByReasonItem("SUDDEN_ACCELERATION", 50),
                createTotalEventCntByReasonItem("SUDDEN_STOP", 30)
        );

        when(analysisClient.getTotalEventCntByType()).thenReturn(expectedEvents);

        // when
        List<TotalEventCntByReasonItem> result = analysisFetchService.getTotalEventCntByType();

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(expectedEvents, result);
        verify(analysisClient, times(1)).getTotalEventCntByType();
    }

    /**
     * getTotalEventCntByType(List<String> driveIds) 테스트 - 다중 운전 ID의 이벤트 발생 횟수 조회
     * 시나리오: 여러 운전 ID 목록으로 각 운전의 이벤트 발생 횟수를 조회하는 경우
     * 기대:
     *  - AnalysisClient의 driveIds 파라미터를 받는 getTotalEventCntByType 메서드가 정확한 파라미터로 1회 호출됨
     *  - 클라이언트로부터 받은 응답 Map이 그대로 반환됨
     *  - 반환된 Map의 키는 운전 ID이고 값은 해당 운전의 이벤트 목록임
     */
    @Test
    @DisplayName("다중 운전 ID의 이벤트 발생 횟수 조회")
    void getTotalEventCntByType_withMultipleDriveIds_shouldReturnEventsMap() {
        // given
        List<String> driveIds = Arrays.asList("drive-123", "drive-456");

        Map<String, List<EventsByDriveDTO>> expectedEventsMap = new HashMap<>();
        expectedEventsMap.put("drive-123", Arrays.asList(
                createEventsByDriveDTO("SPEEDING", 5),
                createEventsByDriveDTO("SUDDEN_ACCELERATION", 3)
        ));
        expectedEventsMap.put("drive-456", Arrays.asList(
                createEventsByDriveDTO("SUDDEN_STOP", 2),
                createEventsByDriveDTO("SPEEDING", 4)
        ));

        when(analysisClient.getTotalEventCntByType(driveIds)).thenReturn(expectedEventsMap);

        // when
        Map<String, List<EventsByDriveDTO>> result = analysisFetchService.getTotalEventCntByType(driveIds);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedEventsMap, result);
        verify(analysisClient, times(1)).getTotalEventCntByType(driveIds);
    }

    // 테스트용 EventsByDriveDTO 객체 생성 헬퍼 메서드
    private EventsByDriveDTO createEventsByDriveDTO(String eventType, int count) {
        return EventsByDriveDTO.builder()
                .type(eventType)
                .count((long) count)
                .build();
    }

    // 테스트용 TotalEventCntByReasonItem 객체 생성 헬퍼 메서드
    private TotalEventCntByReasonItem createTotalEventCntByReasonItem(String reason, int count) {
        return TotalEventCntByReasonItem.builder()
                .reason(reason)
                .count((long) count)
                .build();
    }
}
