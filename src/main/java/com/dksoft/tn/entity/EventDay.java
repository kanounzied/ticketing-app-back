package com.dksoft.tn.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Data
@Table(name = "event_days")
public class EventDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(nullable = false)
    private int maxNumber;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonBackReference("event-eventday")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    @OneToMany(mappedBy = "eventDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EventDayTicketType> eventDayTicketTypes = new ArrayList<>();
}