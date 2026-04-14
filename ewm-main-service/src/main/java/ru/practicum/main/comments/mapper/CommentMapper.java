package ru.practicum.main.comments.mapper;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import ru.practicum.main.comments.dto.CommentDto;
import ru.practicum.main.comments.dto.NewCommentDto;
import ru.practicum.main.comments.model.Comment;
import ru.practicum.main.events.dto.EventShortDto;
import ru.practicum.main.events.model.Event;
import ru.practicum.main.user.dto.UserShortDto;
import ru.practicum.main.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {
    public @NonNull Comment toComment(NewCommentDto newCommentDto, User author, Event event) {
        return Comment.builder()
                .author(author)
                .event(event)
                .text(newCommentDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public @NonNull CommentDto toCommentDto(Comment comment, UserShortDto author, EventShortDto event, long likesCount) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(author)
                .event(event)
                .created(comment.getCreated())
                .edited(comment.getEdited())
                .likesCount(likesCount)
                .build();
    }
}