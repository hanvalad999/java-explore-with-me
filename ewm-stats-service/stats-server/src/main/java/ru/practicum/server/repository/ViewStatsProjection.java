package ru.practicum.server.repository;

public interface ViewStatsProjection {
    String getApp();

    String getUri();

    Long getHits();
}
