package com.modive.adminservice.domain.event.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "event_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventType {
    @Id
    private Long eventTypeId;
    private String eventTypeName;
    private String eventTypeCode;
}
