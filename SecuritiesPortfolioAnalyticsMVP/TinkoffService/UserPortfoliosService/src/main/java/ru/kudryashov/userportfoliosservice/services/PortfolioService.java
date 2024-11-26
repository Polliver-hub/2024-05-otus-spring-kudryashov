package ru.kudryashov.userportfoliosservice.services;

import ru.kudryashov.userportfoliosservice.dto.PortfolioDto;
import ru.kudryashov.userportfoliosservice.dto.PositionDto;
import ru.kudryashov.userportfoliosservice.models.Portfolio;

import java.util.List;

public interface PortfolioService {

    String createNewPortfolioByUserId(String userId, PortfolioDto portfolio);

    Portfolio getPortfolioById(String id);

    List<Portfolio> getAllPortfolioByUserId(String userId);

    String createPositionInPortfolioById(String id, PositionDto position);

    String updatePositionInPortfolioById(String id, PositionDto position);
}
