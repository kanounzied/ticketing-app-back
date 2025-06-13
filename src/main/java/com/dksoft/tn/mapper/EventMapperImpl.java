package com.dksoft.tn.mapper;

import com.dksoft.tn.dto.EventDto;
import com.dksoft.tn.entity.*;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;

@Service
public class EventMapperImpl implements EventMapper {

    private final EventDayMapper eventDayMapper;

    public EventMapperImpl(EventDayMapper eventDayMapper) {
        this.eventDayMapper = eventDayMapper;
    }

    @Override
    public Event fromEventDTO(@NonNull EventDto dto) {
        List<Category> categories = dto.categoryIds() != null ? dto.categoryIds().stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toList()) : null;

        User organizer = new User();
        organizer.setId(dto.organizerId());

        List<EventDay> eventDays = dto.eventDays() != null ? dto.eventDays().stream()
                .map(eventDayMapper::fromEventDayDto)
                .collect(Collectors.toList()) : null;

        return Event.builder()
                .id(dto.id())
                .imageUrls(dto.imageUrls())
                .title(dto.title())
                .description(dto.description())
                .shortDescription(dto.shortDescription())
                .categories(categories)
                .organizer(organizer)
                .isPublished(dto.isPublished())
                .createdAt(dto.createdAt())
                .updatedAt(dto.updatedAt())
                .tags(dto.tags())
                .eventDays(eventDays)
                .build();
    }

    @Override
    public EventDto fromEvent(Event event) {
        List<com.dksoft.tn.dto.EventDayDto> eventDayDtos = event.getEventDays() != null ? event.getEventDays().stream()
                .map(eventDayMapper::fromEventDay)
                .collect(Collectors.toList()) : null;

        List<Long> categoryIds = event.getCategories() != null ? event.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList()) : null;

        return new EventDto(
                event.getId(),
                event.getImageUrls(),
                event.getTitle(),
                event.getDescription(),
                event.getShortDescription(),
                categoryIds,
                event.getOrganizer() != null ? event.getOrganizer().getId() : null,
                event.isPublished(),
                event.getCreatedAt(),
                event.getUpdatedAt(),
                event.getTags(),
                eventDayDtos
        );
    }
}