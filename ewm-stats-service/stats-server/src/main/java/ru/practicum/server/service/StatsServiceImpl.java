package ru.practicum.server.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.mapper.StatsMapper;
import ru.practicum.server.repository.EndpointHitRepository;
import ru.practicum.server.repository.ViewStatsProjection;

@Service
public class StatsServiceImpl implements StatsService {
    private final EndpointHitRepository endpointHitRepository;

    public StatsServiceImpl(EndpointHitRepository endpointHitRepository) {
        this.endpointHitRepository = endpointHitRepository;
    }

    @Override
    @Transactional
    public void saveHit(EndpointHitDto endpointHitDto) {
        endpointHitRepository.save(StatsMapper.toEntity(endpointHitDto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        List<ViewStatsProjection> projections;
        boolean hasUris = uris != null && !uris.isEmpty();

        if (unique && hasUris) {
            projections = endpointHitRepository.findUniqueStatsByUris(start, end, uris);
        } else if (unique) {
            projections = endpointHitRepository.findUniqueStats(start, end);
        } else if (hasUris) {
            projections = endpointHitRepository.findStatsByUris(start, end, uris);
        } else {
            projections = endpointHitRepository.findStats(start, end);
        }

        if (projections.isEmpty()) {
            return Collections.emptyList();
        }

        return projections.stream()
                .map(StatsMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }
}
