package ru.practicum.main.compilation.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.events.mapper.EventMapper;
import ru.practicum.main.events.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(compilation.getEvents().stream()
                        .map(EventMapper::toEventShortDto)
                        .collect(Collectors.toList()))
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .build();
    }

    public static Compilation toCompilation(CompilationDto compilationDto, Set<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setId(compilation.getId());
        compilation.setEvents(events);
        compilation.setPinned(compilationDto.getPinned() != null ? compilationDto.getPinned() : false);
        compilation.setTitle(compilationDto.getTitle());

        return compilation;
    }
}