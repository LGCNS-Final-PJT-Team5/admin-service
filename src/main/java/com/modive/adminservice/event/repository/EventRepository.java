package com.modive.adminservice.event.repository;

import com.modive.adminservice.event.dto.EventTotalCntByTypeDTO;
import com.modive.adminservice.event.dto.EventsByDriveDTO;
import com.modive.adminservice.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT new com.modive.adminservice.event.dto.EventsByDriveDTO(et.eventTypeName, COUNT(e)) " +
            "FROM Event e INNER JOIN e.eventType et " +
            "WHERE e.driveId = :driveId " +
            "GROUP BY et.eventTypeId, et.eventTypeName")
    List<EventsByDriveDTO> countByTypeGroupedByDriveId(@Param("driveId") Long driveId);

    @Query("SELECT new com.modive.adminservice.event.dto.EventTotalCntByTypeDTO(et.eventTypeName, COUNT(e)) " +
            "FROM Event e JOIN e.eventType et " +
            "GROUP BY et.eventTypeId")
    List<EventTotalCntByTypeDTO> totalCntByType();
}