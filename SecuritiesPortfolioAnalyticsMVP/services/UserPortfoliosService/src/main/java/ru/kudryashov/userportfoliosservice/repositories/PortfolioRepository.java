package ru.kudryashov.userportfoliosservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.kudryashov.userportfoliosservice.models.Portfolio;

import java.util.List;

public interface PortfolioRepository extends MongoRepository<Portfolio, String> {

    List<Portfolio> findAllByUserId(String userId);

}
