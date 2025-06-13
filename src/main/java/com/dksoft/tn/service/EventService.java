package com.dksoft.tn.service;

import com.dksoft.tn.entity.Event;
import com.dksoft.tn.exception.EventNotFound;
import com.dksoft.tn.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event createEvent(Event event) {
        validateEvent(event);
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(long id) {
        return eventRepository.findById(id);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Event updateEvent(long id, Event event) throws EventNotFound {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFound("Event not found with id: " + id));
        validateEvent(event);

        existingEvent.setImageUrls(event.getImageUrls() != null ? event.getImageUrls() : new ArrayList<>());
        existingEvent.setTitle(event.getTitle());
        existingEvent.setDescription(event.getDescription());
        existingEvent.setShortDescription(event.getShortDescription());
        existingEvent.setCategories(event.getCategories());
        existingEvent.setOrganizer(event.getOrganizer());
        existingEvent.setIsPublished(event.isPublished());
        existingEvent.setTags(event.getTags());
        existingEvent.setEventDays(event.getEventDays());

        if (event.getEventDays() != null) {
            event.getEventDays().forEach(eventDay -> eventDay.setEvent(existingEvent));
        }

        return eventRepository.save(existingEvent);
    }

    public void deleteEvent(long id) throws EventNotFound {
        if (!eventRepository.existsById(id)) {
            throw new EventNotFound("Event not found with id: " + id);
        }
        eventRepository.deleteById(id);
    }

    private void validateEvent(Event event) {
        if (event.getTitle() == null || event.getTitle().isBlank()) {
            throw new IllegalArgumentException("Event title cannot be empty");
        }
        if (event.getDescription() == null || event.getDescription().isBlank()) {
            throw new IllegalArgumentException("Event description cannot be empty");
        }
        if (event.getShortDescription() == null || event.getShortDescription().isBlank()) {
            throw new IllegalArgumentException("Event short description cannot be empty");
        }
        if (event.getOrganizer() == null) {
            throw new IllegalArgumentException("Event organizer cannot be null");
        }
        if (event.getEventDays() == null || event.getEventDays().isEmpty()) {
            throw new IllegalArgumentException("Event must have at least one event day");
        }
        // Initialize empty ticket types if null
        event.getEventDays().forEach(eventDay -> {
            if (eventDay.getEventDayTicketTypes() == null) {
                eventDay.setEventDayTicketTypes(new ArrayList<>());
            }
        });
//        boolean hasTicketType = event.getEventDays().stream()
//                .anyMatch(eventDay -> !eventDay.getEventDayTicketTypes().isEmpty());
//        if (!hasTicketType) {
//            throw new IllegalArgumentException("At least one event day must have a ticket type");
//        }
        boolean hasPlace = event.getEventDays().stream()
                .allMatch(eventDay -> eventDay.getPlace() != null);
        if (!hasPlace) {
            throw new IllegalArgumentException("Each event day must have a place");
        }
    }
}