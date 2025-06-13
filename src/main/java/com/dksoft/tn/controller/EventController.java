package com.dksoft.tn.controller;

import com.dksoft.tn.dto.EventDto;
import com.dksoft.tn.entity.Event;
import com.dksoft.tn.exception.EventNotFound;
import com.dksoft.tn.mapper.EventMapper;
import com.dksoft.tn.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private final EventMapper eventMapper;

    public EventController(EventService eventService, EventMapper eventMapper) {
        this.eventService = eventService;
        this.eventMapper = eventMapper;
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDto> createEvent(
            @RequestBody EventDto eventDto
    ) throws IOException {
        Event event = eventMapper.fromEventDTO(eventDto);
        Event savedEvent = eventService.createEvent(event);
        return ResponseEntity.ok(eventMapper.fromEvent(savedEvent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable long id) throws EventNotFound {
        Event event = eventService.getEventById(id)
                .orElseThrow(() -> new EventNotFound("Event not found with id: " + id));
        return ResponseEntity.ok(eventMapper.fromEvent(event));
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        List<EventDto> eventDtos = events.stream()
                .map(eventMapper::fromEvent)
                .toList();
        return ResponseEntity.ok(eventDtos);
    }

    @PutMapping(value = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDto> updateEvent(@PathVariable long id,
                                                @Valid @RequestPart("event") EventDto eventDto,
                                                @RequestPart(value = "images", required = false) MultipartFile[] images) throws EventNotFound, IOException {
        Event event = eventMapper.fromEventDTO(eventDto);
        Event updatedEvent = eventService.updateEvent(id, event);
        return ResponseEntity.ok(eventMapper.fromEvent(updatedEvent));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable long id) throws EventNotFound {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}