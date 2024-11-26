package ru.kudryashov.userportfoliosservice.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.kudryashov.userportfoliosservice.models.Instrument;

public interface InstrumentRepository extends MongoRepository<Instrument, String> {
}
