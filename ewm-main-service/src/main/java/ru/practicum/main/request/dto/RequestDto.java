package ru.practicum.main.request.dto;

import lombok.*;
import ru.practicum.main.request.mapper.StatusRequest;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {
    private Long id;
    private Long requester;
    private Long event;
    private StatusRequest status;
    private LocalDateTime created;
}