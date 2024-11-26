package ru.kudryashov.userportfoliosservice.repositories;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.kudryashov.userportfoliosservice.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
