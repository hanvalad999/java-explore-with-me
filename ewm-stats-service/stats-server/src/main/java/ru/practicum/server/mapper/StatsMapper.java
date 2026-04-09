package ru.practicum.server.mapper;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.server.model.EndpointHit;
import ru.practicum.server.repository.ViewStatsProjection;

public final class StatsMapper {
    private StatsMapper() {
    }

    public static EndpointHit toEntity(EndpointHitDto dto) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setId(dto.getId());
        endpointHit.setApp(dto.getApp());
        endpointHit.setUri(dto.getUri());
        endpointHit.setIp(dto.getIp());
        endpointHit.setTimestamp(dto.getTimestamp());
        return endpointHit;
    }

    public static ViewStatsDto toViewStatsDto(ViewStatsProjection projection) {
        return new ViewStatsDto(projection.getApp(), projection.getUri(), projection.getHits());
    }
}
