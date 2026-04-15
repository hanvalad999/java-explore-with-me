package ru.practicum.main.events.service;

import ru.practicum.main.events.dto.*;
import ru.practicum.main.request.dto.RequestDto;

import java.util.List;

public interface EventPrivateService {
    List<EventShortDto> getUserEvents(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    EventFullDto getUserEventById(Long userId, Long eventId);

    EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<RequestDto> getEventRequests(Long userId, Long eventId);

    EventRequestStatusUpdateResult changeRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}