package com.dksoft.tn.dto;

import java.time.LocalDateTime;
import java.util.List;

public record EventDto(
        long id,
        List<String> imageUrls,
        String title,
        String description,
        String shortDescription,
        List<Long> categoryIds,
        Long organizerId,
        boolean isPublished,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<String> tags,
        List<EventDayDto> eventDays
) {
}