package ru.kudryashov.userportfoliosservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "portfolios")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {

    @Id
    private String id;

    private String name;

    private String userId;

    private List<Position> positions;
}
