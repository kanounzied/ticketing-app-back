package com.dksoft.tn.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDayDto(
        long id,
        LocalDateTime dateTime,
        boolean isActive,
        int maxNumber,
        Long eventId,
        Long placeId,
        List<EventDayTicketTypeDto> eventDayTicketTypes
) {
}