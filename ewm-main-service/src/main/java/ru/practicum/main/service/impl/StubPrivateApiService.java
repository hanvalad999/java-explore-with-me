package ru.practicum.main.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.dto.EventRequestStatusUpdateResult;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.dto.NewEventDto;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.dto.UpdateEventUserRequest;
import ru.practicum.main.service.PrivateApiService;

@Service
public class StubPrivateApiService implements PrivateApiService {
    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto request) {
        return EventFullDto.builder().build();
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        return EventFullDto.builder().id(eventId).build();
    }

    @Override
    public EventFullDto updateUserEvent(Long userId, Long eventId, UpdateEventUserRequest request) {
        return EventFullDto.builder().id(eventId).build();
    }

    @Override
    public List<ParticipationRequestDto> getUserEventRequests(Long userId, Long eventId) {
        return List.of();
    }

    @Override
    public EventRequestStatusUpdateResult updateUserEventRequests(Long userId, Long eventId,
                                                                  EventRequestStatusUpdateRequest request) {
        return EventRequestStatusUpdateResult.builder().confirmedRequests(List.of()).rejectedRequests(List.of()).build();
    }

    @Override
    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        return List.of();
    }

    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        return ParticipationRequestDto.builder().event(eventId).requester(userId).build();
    }

    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        return ParticipationRequestDto.builder().id(requestId).requester(userId).status("CANCELED").build();
    }
}
