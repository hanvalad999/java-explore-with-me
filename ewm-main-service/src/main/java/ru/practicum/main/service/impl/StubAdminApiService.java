package ru.practicum.main.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.dto.UpdateEventAdminRequest;
import ru.practicum.main.dto.UserDto;
import ru.practicum.main.service.AdminApiService;

@Service
public class StubAdminApiService implements AdminApiService {
    @Override
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        return List.of();
    }

    @Override
    public UserDto createUser(NewUserRequest request) {
        return UserDto.builder().name(request.getName()).email(request.getEmail()).build();
    }

    @Override
    public void deleteUser(Long userId) {
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto request) {
        return CategoryDto.builder().name(request.getName()).build();
    }

    @Override
    public CategoryDto updateCategory(Long categoryId, NewCategoryDto request) {
        return CategoryDto.builder().id(categoryId).name(request.getName()).build();
    }

    @Override
    public void deleteCategory(Long categoryId) {
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto request) {
        return CompilationDto.builder().title(request.getTitle()).pinned(Boolean.TRUE.equals(request.getPinned())).build();
    }

    @Override
    public void deleteCompilation(Long compilationId) {
    }

    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest request) {
        return CompilationDto.builder().id(compilationId).title(request.getTitle()).pinned(request.getPinned()).build();
    }

    @Override
    public List<EventFullDto> searchEvents(List<Long> users, List<String> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size) {
        return List.of();
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request) {
        return EventFullDto.builder().id(eventId).build();
    }
}
