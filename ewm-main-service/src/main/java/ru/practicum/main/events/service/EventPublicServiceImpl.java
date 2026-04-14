package ru.practicum.main.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.client.StatsClient;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.main.common.OffsetPageRequest;
import ru.practicum.main.error.BadRequestException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.events.dto.EventFullDto;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.mapper.EventMapper;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventSort;
import ru.practicum.main.events.model.EventState;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.events.repository.EventSpecification;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventPublicServiceImpl implements EventPublicService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;

    @Override
    public List<EventShortDto> getPublicEvents(String text, List<Long> categories, Boolean paid,
                                               LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                               EventSort sort, int from, int size, HttpServletRequest httpRequest) {
        Pageable pageable = (sort == EventSort.EVENT_DATE)
                ? new OffsetPageRequest(from, size, Sort.by("eventDate").ascending())
                : new OffsetPageRequest(from, size);

        if (rangeStart != null && rangeEnd != null && rangeEnd.isBefore(rangeStart)) {
            throw new BadRequestException("rangeEnd must not be before rangeStart");
        }

        LocalDateTime effectiveStart = rangeStart != null ? rangeStart : LocalDateTime.now();

        Specification<Event> spec = Specification
                .where(EventSpecification.hasState(EventState.PUBLISHED))
                .and(EventSpecification.hasText(text))
                .and(EventSpecification.hasCategories(categories))
                .and(EventSpecification.hasPaid(paid))
                .and(EventSpecification.eventDateAfter(effectiveStart))
                .and(EventSpecification.eventDateBefore(rangeEnd))
                .and(EventSpecification.isAvailable(onlyAvailable));

        List<Event> events = sort == EventSort.VIEWS
                ? eventRepository.findAll(spec)
                : eventRepository.findAll(spec, pageable).getContent();

        saveHit(httpRequest);

        Map<Long, Long> viewsMap = getViewsMap(events);

        List<EventShortDto> result = events.stream()
                .map(e -> {
                    EventShortDto dto = EventMapper.toEventShortDto(e);
                    dto.setViews(viewsMap.getOrDefault(e.getId(), e.getViews()));
                    return dto;
                })
                .collect(Collectors.toList());

        if (sort == EventSort.VIEWS) {
            result.sort(Comparator.comparingLong(EventShortDto::getViews).reversed());
            return result.stream()
                    .skip(from)
                    .limit(size)
                    .toList();
        }

        return result;
    }

    @Override
    @Transactional
    public EventFullDto getPublicEventById(Long eventId, HttpServletRequest httpRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        saveHit(httpRequest);

        long views = 0L;
        try {
            List<ViewStatsDto> stats = statsClient.getStats(
                    getStatsStart(event),
                    LocalDateTime.now().plusDays(3),
                    List.of("/events/" + eventId),
                    true);
            views = stats.isEmpty() ? event.getViews() : stats.getFirst().getHits();
            event.setViews(views);
            eventRepository.save(event);
        } catch (Exception e) {
            log.warn("Failed to fetch view stats for event id={}: {}", eventId, e.getMessage(), e);
            views = event.getViews();
        }

        EventFullDto dto = EventMapper.toEventFullDto(event);
        dto.setViews(views);
        return dto;
    }

    private LocalDateTime getStatsStart(Event event) {
        LocalDateTime start = event.getPublishedOn() != null ? event.getPublishedOn() : event.getCreatedOn();
        return start.minusSeconds(1);
    }

    private void saveHit(HttpServletRequest request) {
        try {
            statsClient.saveHit(new EndpointHitDto(
                    null,
                    "ewm-main-service",
                    request.getRequestURI(),
                    request.getRemoteAddr(),
                    LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            ));
        } catch (Exception e) {
            log.warn("Failed to save hit for uri={} ip={}: {}", request.getRequestURI(), request.getRemoteAddr(),
                    e.getMessage(), e);
        }
    }

    private Map<Long, Long> getViewsMap(List<Event> events) {
        if (events.isEmpty()) return Map.of();
        try {
            List<String> uris = events.stream()
                    .map(e -> "/events/" + e.getId())
                    .toList();
            List<ViewStatsDto> stats = statsClient.getStats(
                    getStatsStart(events),
                    LocalDateTime.now().plusMonths(2),
                    uris,
                    true
            );
            return stats.stream()
                    .collect(Collectors.toMap(
                            s -> Long.parseLong(s.getUri().replace("/events/", "")),
                            ViewStatsDto::getHits
                    ));
        } catch (Exception e) {
            log.warn("Failed to fetch view stats for {} uris: {}", events.size(), e.getMessage(), e);
            return Map.of();
        }
    }

    private LocalDateTime getStatsStart(List<Event> events) {
        return events.stream()
                .map(this::getStatsStart)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(1));
    }
}
