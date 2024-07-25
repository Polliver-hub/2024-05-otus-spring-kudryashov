package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> findAll() {
        TypedQuery<Book> query = entityManager.createQuery(
                "SELECT b FROM Book b " +
                        "LEFT JOIN FETCH b.author " +
                        "LEFT JOIN FETCH b.genre " +
                        "LEFT JOIN FETCH b.comments " +
                        "ORDER BY b.title",
                Book.class);
        return query.getResultList();
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            entityManager.persist(book);
            return book;
        }
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        Optional<Book> optionalBook = Optional.ofNullable(
                entityManager.find(Book.class, id));
        if (optionalBook.isPresent()) {
            entityManager.remove(optionalBook.get());
        } else {
            throw new EntityNotFoundException("No record found with id: " + id);
        }
    }
}
