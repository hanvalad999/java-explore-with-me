package ru.practicum.main.request.service;

import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.user.dto.NewUserRequest;

import java.util.List;

public interface RequestServiceImpl {

    RequestDto addUser(NewUserRequest request);

    List<RequestDto> getUsers(List<Long> ids, int from, int size);

    void deleteUser(long userId);
}