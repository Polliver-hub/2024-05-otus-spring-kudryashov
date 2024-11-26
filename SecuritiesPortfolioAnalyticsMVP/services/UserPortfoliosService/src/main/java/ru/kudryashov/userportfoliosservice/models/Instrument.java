package ru.kudryashov.userportfoliosservice.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "instruments")
public class Instrument {

    @Id
    private String id;

    private String name;

    private String ticker;

    private String figi;

    private String type;

    private String currency;
}
