package ru.practicum.main.events.dto;

import lombok.*;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import ru.practicum.main.request.mapper.StatusRequest;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {

    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    private StatusRequest status;
    // CONFIRMED / REJECTED
}