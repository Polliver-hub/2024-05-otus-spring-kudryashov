package ru.kudryashov.userportfoliosservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kudryashov.userportfoliosservice.dto.PortfolioDto;
import ru.kudryashov.userportfoliosservice.dto.PositionDto;
import ru.kudryashov.userportfoliosservice.exceptions.EntityNotFoundException;
import ru.kudryashov.userportfoliosservice.models.Portfolio;
import ru.kudryashov.userportfoliosservice.models.Position;
import ru.kudryashov.userportfoliosservice.repositories.PortfolioRepository;
import ru.kudryashov.userportfoliosservice.services.PortfolioService;
import ru.kudryashov.userportfoliosservice.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private final PortfolioRepository portfolioRepository;

    private final UserService userService;

    @Override
    public String createNewPortfolioByUserId(String userId, PortfolioDto portfolio) {
        Portfolio portfolioToSave = Portfolio.builder()
                .userId(userId)
                .name(portfolio.getName())
                .positions(List.of())
                .build();
        var savedPortfolio = portfolioRepository.save(portfolioToSave);
        userService.addNewPortfolio(userId, savedPortfolio);
        return savedPortfolio.getId();
    }

    @Override
    public Portfolio getPortfolioById(String id) {
        return portfolioRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Portfolio> getAllPortfolioByUserId(String userId) {
        return portfolioRepository.findAllByUserId(userId);
    }

    @Override
    public String createPositionInPortfolioById(String id, PositionDto position) {
        // find by ticker in instrumentService
        var portfolioForAddPosition = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio with id %s not found"
                        .formatted(id)));
        Position newPosition = new Position(position.getTicker(), position.getQuantity());
        portfolioForAddPosition.getPositions().add(newPosition);
        var portfolioWithNewPosition = portfolioRepository.save(portfolioForAddPosition);
        return portfolioWithNewPosition.getId();
    }

    @Override
    public String updatePositionInPortfolioById(String id, PositionDto position) {
        // find by ticker in instrumentService
        var portfolioForUpdatePosition = portfolioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Portfolio with id %s not found".formatted(id)));

        portfolioForUpdatePosition.getPositions().stream()
                .filter(pos -> pos.getTicker().equals(position.getTicker()))
                .findFirst().ifPresentOrElse(
                        pos -> pos.setQuantity(position.getQuantity()),
                        () -> {
                            throw new EntityNotFoundException("Position with ticker %s not found"
                                    .formatted(position.getTicker()));
                        });

        var portfolioWithUpdatedPosition = portfolioRepository.save(portfolioForUpdatePosition);
        return portfolioWithUpdatedPosition.getId();
    }


}
