package ru.practicum.server.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.server.model.EndpointHit;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(h.id) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
            GROUP BY h.app, h.uri
            ORDER BY hits DESC
            """)
    List<ViewStatsProjection> findStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(h.id) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris
            GROUP BY h.app, h.uri
            ORDER BY hits DESC
            """)
    List<ViewStatsProjection> findStatsByUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end
            GROUP BY h.app, h.uri
            ORDER BY hits DESC
            """)
    List<ViewStatsProjection> findUniqueStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
            SELECT h.app AS app, h.uri AS uri, COUNT(DISTINCT h.ip) AS hits
            FROM EndpointHit h
            WHERE h.timestamp BETWEEN :start AND :end AND h.uri IN :uris
            GROUP BY h.app, h.uri
            ORDER BY hits DESC
            """)
    List<ViewStatsProjection> findUniqueStatsByUris(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("uris") List<String> uris
    );
}
