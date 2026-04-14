package ru.practicum.main.service;

import java.time.LocalDateTime;
import java.util.List;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.NewUserRequest;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.dto.UpdateEventAdminRequest;
import ru.practicum.main.dto.UserDto;

public interface AdminApiService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto createUser(NewUserRequest request);

    void deleteUser(Long userId);

    CategoryDto createCategory(NewCategoryDto request);

    CategoryDto updateCategory(Long categoryId, NewCategoryDto request);

    void deleteCategory(Long categoryId);

    CompilationDto createCompilation(NewCompilationDto request);

    void deleteCompilation(Long compilationId);

    CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest request);

    List<EventFullDto> searchEvents(List<Long> users,
                                    List<String> states,
                                    List<Long> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    int from,
                                    int size);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest request);
}
