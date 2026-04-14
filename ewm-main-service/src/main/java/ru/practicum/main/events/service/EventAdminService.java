package ru.practicum.main.events.service;

import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventAdminService {
    List<EventFullDto> getEventsAdmin(List<Long> users, List<String> states, List<Long> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventAdminRequest request);
}