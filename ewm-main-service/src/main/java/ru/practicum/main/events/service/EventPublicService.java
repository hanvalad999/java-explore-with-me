package ru.practicum.main.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.model.EventSort;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                        EventSort sort, int from, int size, HttpServletRequest httpRequest);

    EventFullDto getPublicEventById(Long eventId, HttpServletRequest httpRequest);
}