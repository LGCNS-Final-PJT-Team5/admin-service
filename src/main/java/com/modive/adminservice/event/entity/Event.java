package com.modive.adminservice.event.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    private Long eventId;
    private Long userId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_id")
    private EventType eventType;

    private LocalDateTime time;

    // TODO 타입 확인
    private String gnss;

    private Long driveId;
}
