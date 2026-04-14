package ru.practicum.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.service.PublicApiService;

@Service
public class StubPublicApiService implements PublicApiService {
    @Override
    public List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto getEventById(Long eventId) {
        return EventFullDto.builder().id(eventId).build();
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        return List.of();
    }

    @Override
    public CategoryDto getCategoryById(Long categoryId) {
        return CategoryDto.builder().id(categoryId).build();
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        return List.of();
    }

    @Override
    public CompilationDto getCompilationById(Long compilationId) {
        return CompilationDto.builder().id(compilationId).build();
    }
}
