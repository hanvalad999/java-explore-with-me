package ru.practicum.main.comments.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.main.comments.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
            SELECT c
            FROM Comment c
            LEFT JOIN CommentLike cl ON cl.comment = c
            WHERE c.event.id = :eventId
            GROUP BY c
            """, countQuery = "SELECT COUNT(DISTINCT c) FROM Comment c WHERE c.event.id = :eventId")
    Page<Comment> findAllByEventIdOrderByLikes(@Param("eventId") Long eventId, Pageable pageable);

    @Query(value = """
            SELECT c
            FROM Comment c
            LEFT JOIN CommentLike cl ON cl.comment = c
            WHERE c.author.id = :userId
            GROUP BY c
            """, countQuery = "SELECT COUNT(DISTINCT c) FROM Comment c WHERE c.author.id = :userId")
    Page<Comment> findAllByAuthorIdOrderByLikes(@Param("userId") Long userId, Pageable pageable);
}
