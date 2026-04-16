package ru.practicum.main.comments.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.comments.dto.CommentDto;
import ru.practicum.main.comments.dto.NewCommentDto;
import ru.practicum.main.comments.mapper.CommentMapper;
import ru.practicum.main.comments.model.Comment;
import ru.practicum.main.comments.model.CommentLike;
import ru.practicum.main.comments.model.Sort;
import ru.practicum.main.comments.repository.CommentLikeRepository;
import ru.practicum.main.comments.repository.CommentRepository;
import ru.practicum.main.error.ConflictException;
import ru.practicum.main.error.NotFoundException;
import ru.practicum.main.events.mapper.EventMapper;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.events.model.EventState;
import ru.practicum.main.events.repository.EventRepository;
import ru.practicum.main.user.mapper.UserMapper;
import ru.practicum.main.user.model.User;
import ru.practicum.main.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        log.info("Creating comment for user: {}, event: {}", userId, eventId);
        User author = checkAndGetUser(userId);
        Event event = checkAndGetEvent(eventId);

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Comments are only allowed on published events.");
        }

        Comment comment = commentRepository.save(CommentMapper.toComment(newCommentDto, author, event));
        log.debug("Comment created with id: {}", comment.getId());

        return CommentMapper.toCommentDto(
                comment,
                UserMapper.toUserShortDto(author),
                EventMapper.toEventShortDto(event),
                0L
        );
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, NewCommentDto newCommentDto) {
        User author = checkAndGetUser(userId);
        Comment comment = checkAndGetComment(commentId);
        log.info("Updating comment for user: {}, commentId: {}", userId, commentId);

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only the author can edit the comment.");
        }

        comment.setText(newCommentDto.getText());
        comment.setEdited(LocalDateTime.now());

        long countLikes = commentLikeRepository.countByCommentId(comment.getId());
        return CommentMapper.toCommentDto(
                comment,
                UserMapper.toUserShortDto(author),
                EventMapper.toEventShortDto(comment.getEvent()),
                countLikes
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByAuthorId(Long userId, Integer from, Integer size, Sort sortParam) {
        log.info("Getting comments from user sorted by likes: userId={}, from={}, size={}",
                userId, from, size);

        checkAndGetUser(userId);

        Pageable pageable = createLikesPageable(from, size, sortParam);

        Page<Comment> page = commentRepository.findAllByAuthorIdOrderByLikes(userId, pageable);
        return mapCommentsToDtos(page.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByEventId(Long eventId, Integer from, Integer size, Sort sortParam) {
        log.info("Getting comments for event sorted by likes: eventId={}, from={}, size={}",
                eventId, from, size);

        checkAndGetEvent(eventId);

        Pageable pageable = createLikesPageable(from, size, sortParam);

        Page<Comment> page = commentRepository.findAllByEventIdOrderByLikes(eventId, pageable);
        return mapCommentsToDtos(page.getContent());
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDto getCommentById(Long commentId) {
        log.info("Getting comment with id={}", commentId);
        Comment comment = checkAndGetComment(commentId);
        long countLikes = commentLikeRepository.countByCommentId(comment.getId());

        return CommentMapper.toCommentDto(
                comment,
                UserMapper.toUserShortDto(comment.getAuthor()),
                EventMapper.toEventShortDto(comment.getEvent()),
                countLikes
        );
    }

    @Override
    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        log.info("Delete comment by a user: userId={}, commentId={}", userId, commentId);
        Comment comment = checkAndGetComment(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("Only author can delete the comment.");
        }
        commentLikeRepository.deleteAllByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Delete comment with id={}", commentId);
        checkAndGetComment(commentId);
        commentLikeRepository.deleteAllByCommentId(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public CommentDto addLike(Long userId, Long commentId) {
        User author = checkAndGetUser(userId);
        Comment comment = checkAndGetComment(commentId);

        if (comment.getAuthor().getId().equals(userId)) {
            throw new ConflictException("You cannot like your own comment.");
        }
        if (commentLikeRepository.existsByUserIdAndCommentId(userId, commentId)) {
            throw new ConflictException("You already liked this comment");
        }

        commentLikeRepository.save(CommentLike.builder().user(author).comment(comment).build());
        long likesCount = commentLikeRepository.countByCommentId(commentId);

        return CommentMapper.toCommentDto(
                comment,
                UserMapper.toUserShortDto(comment.getAuthor()),
                EventMapper.toEventShortDto(comment.getEvent()),
                likesCount
        );
    }

    @Override
    @Transactional
    public void deleteLike(Long userId, Long commentId) {
        CommentLike like = commentLikeRepository
                .findByUserIdAndCommentId(userId, commentId)
                .orElseThrow(() -> new NotFoundException("Like not found"));
        commentLikeRepository.delete(like);
    }

    private List<CommentDto> mapCommentsToDtos(List<Comment> comments) {
        if (comments.isEmpty()) return List.of();

        List<Long> ids = comments.stream().map(Comment::getId).toList();
        Map<Long, Long> likesMap = commentLikeRepository.countLikesForComments(ids)
                .stream()
                .collect(Collectors.toMap(r -> (Long) r[0], r -> (Long) r[1]));

        return comments.stream()
                .map(c -> CommentMapper.toCommentDto(
                        c,
                        UserMapper.toUserShortDto(c.getAuthor()),
                        EventMapper.toEventShortDto(c.getEvent()),
                        likesMap.getOrDefault(c.getId(), 0L)
                ))
                .toList();
    }

    private Pageable createLikesPageable(Integer from, Integer size, Sort sortParam) {
        org.springframework.data.domain.Sort.Direction direction = sortParam == Sort.ASC
                ? org.springframework.data.domain.Sort.Direction.ASC
                : org.springframework.data.domain.Sort.Direction.DESC;
        return PageRequest.of(from / size, size, JpaSort.unsafe(direction, "COUNT(cl)"));
    }

    private Comment checkAndGetComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new NotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private User checkAndGetUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("User with id=" + userId + " was not found"));
    }

    private Event checkAndGetEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException("Event with id=" + eventId + " was not found"));
    }
}
