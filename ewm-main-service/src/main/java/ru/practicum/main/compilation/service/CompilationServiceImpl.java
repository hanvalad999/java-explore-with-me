package ru.practicum.main.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.common.OffsetPageRequest;
import ru.practicum.main.compilation.dto.CompilationDto;
import ru.practicum.main.compilation.dto.NewCompilationDto;
import ru.practicum.main.compilation.dto.UpdateCompilationRequest;
import ru.practicum.main.compilation.mapper.CompilationMapper;
import ru.practicum.main.compilation.model.Compilation;
import ru.practicum.main.compilation.repository.CompilationRepository;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.repository.EventRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

  private final CompilationRepository compilationRepository;
  private final EventRepository eventRepository;

  @Override
  @Transactional
  public CompilationDto saveCompilation(NewCompilationDto dto) {
    log.info("Saving compilation: title={}", dto.getTitle());
    Set<Event> events = resolveEvents(dto.getEvents());
    Compilation compilation = new Compilation();
    compilation.setEvents(events);
    compilation.setPinned(dto.getPinned() != null ? dto.getPinned() : false);
    compilation.setTitle(dto.getTitle());
    return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
  }

  @Override
  @Transactional
  public void deleteCompilation(long compId) {
    log.info("Deleting compilation id={}", compId);
    if (!compilationRepository.existsById(compId)) {
      throw new NotFoundException("Compilation with id=" + compId + " was not found");
    }
    compilationRepository.deleteById(compId);
  }

  @Override
  @Transactional
  public CompilationDto updateCompilation(long compId, UpdateCompilationRequest request) {
    log.info("Updating compilation id={}", compId);
    Compilation compilation = getOrThrow(compId);

    if (request.getEvents() != null) {
      compilation.setEvents(resolveEvents(request.getEvents()));
    }
    if (request.getPinned() != null) {
      compilation.setPinned(request.getPinned());
    }
    if (request.getTitle() != null) {
      compilation.setTitle(request.getTitle());
    }
    return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
  }

  @Override
  public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
    log.info("Getting compilations: pinned={}, from={}, size={}", pinned, from, size);
    Pageable pageable = new OffsetPageRequest(from, size);

    List<Compilation> compilations = pinned != null ?
            compilationRepository.findWithEventsByPinned(pinned, pageable) :
            compilationRepository.findAllWithEvents(pageable);
    return compilations.stream().map(CompilationMapper::toCompilationDto).toList();
  }

  @Override
  public CompilationDto getCompilation(long compId) {
    log.info("Getting compilation id={}", compId);
    return CompilationMapper.toCompilationDto(getOrThrow(compId));
  }

  private Compilation getOrThrow(long compId) {
    return compilationRepository.findById(compId)
            .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
  }

  private Set<Event> resolveEvents(Set<Long> ids) {
    if (ids == null || ids.isEmpty()) {
      return new HashSet<>();
    }
    return new HashSet<>(eventRepository.findAllByIdIn(new ArrayList<>(ids)));
  }
}