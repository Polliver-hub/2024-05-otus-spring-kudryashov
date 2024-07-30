package ru.otus.hw.repositories;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        EntityGraph<?> entityGraph = entityManager.
                getEntityGraph("comments-with-book");
        return Optional.ofNullable(entityManager.find(
                Comment.class,
                id,
                Map.of("jakarta.persistence.fetchgraph", entityGraph)));
    }

    @Override
    public List<Comment> findAllByBookId(long bookId) {
        var query = entityManager.createQuery(
                "SELECT c FROM Comment c WHERE c.book.id = :bookId", Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        Optional<Comment> opComment = Optional.ofNullable(
                entityManager.find(Comment.class, id));
        if (opComment.isPresent()) {
            entityManager.remove(opComment.get());
        } else {
            throw new EntityNotFoundException("No record found with id: " + id);
        }
    }
}
