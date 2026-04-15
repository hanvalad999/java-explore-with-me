package ru.practicum.main.request.model;

import lombok.experimental.UtilityClass;
import ru.practicum.main.request.dto.RequestDto;
import ru.practicum.main.request.mapper.Request;

@UtilityClass
public class RequestMapper {

    public static RequestDto toRequestDto(Request request) {
        if (request == null) {
            return null;
        }
        return RequestDto.builder()
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .id(request.getId())
                .status(request.getStatus())
                .created(request.getCreated())
                .build();
    }
}