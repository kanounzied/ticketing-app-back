package com.dksoft.tn.mapper;

import com.dksoft.tn.dto.EventDayDto;
import com.dksoft.tn.dto.EventDayTicketTypeDto;
import com.dksoft.tn.entity.Event;
import com.dksoft.tn.entity.EventDay;
import com.dksoft.tn.entity.EventDayTicketType;
import com.dksoft.tn.entity.Place;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventDayMapperImpl implements EventDayMapper {

    private final TicketTypeMapper ticketTypeMapper;

    public EventDayMapperImpl(TicketTypeMapper ticketTypeMapper) {
        this.ticketTypeMapper = ticketTypeMapper;
    }

    @Override
    public EventDay fromEventDayDto(@NonNull EventDayDto dto) {
        Event event = null; // Set by EventServiceImpl

        Place place = new Place();
        place.setId(dto.placeId());

        List<EventDayTicketType> eventDayTicketTypes = (dto.eventDayTicketTypes() == null || dto.eventDayTicketTypes().isEmpty())
                ? List.of() // Default to empty list if null or empty
                : dto.eventDayTicketTypes().stream()
                .map(dtoTicket -> {
                    EventDayTicketType eventDayTicketType = new EventDayTicketType();
                    eventDayTicketType.setTicketType(ticketTypeMapper.fromTicketTypeDto(dtoTicket.ticketType()));
                    eventDayTicketType.setMaxNumber(dtoTicket.maxNumber());
                    return eventDayTicketType;
                })
                .collect(Collectors.toList());

        return EventDay.builder()
                .id(dto.id())
                .dateTime(dto.dateTime())
                .isActive(dto.isActive())
                .maxNumber(dto.maxNumber())
                .event(event)
                .place(place)
                .eventDayTicketTypes(eventDayTicketTypes)
                .build();
    }

    @Override
    public EventDayDto fromEventDay(@NonNull EventDay eventDay) {
        List<EventDayTicketTypeDto> eventDayTicketTypeDtos = eventDay.getEventDayTicketTypes() != null
                ? eventDay.getEventDayTicketTypes().stream()
                .map(eventDayTicketType -> new EventDayTicketTypeDto(
                        ticketTypeMapper.fromTicketType(eventDayTicketType.getTicketType()),
                        eventDayTicketType.getMaxNumber()
                ))
                .collect(Collectors.toList())
                : List.of();

        return new EventDayDto(
                eventDay.getId(),
                eventDay.getDateTime(),
                eventDay.isActive(),
                eventDay.getMaxNumber(),
                eventDay.getEvent() != null ? eventDay.getEvent().getId() : null,
                eventDay.getPlace() != null ? eventDay.getPlace().getId() : null,
                eventDayTicketTypeDtos
        );
    }
}