package ru.kudryashov.userportfoliosservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kudryashov.userportfoliosservice.dto.PortfolioDto;
import ru.kudryashov.userportfoliosservice.dto.PositionDto;
import ru.kudryashov.userportfoliosservice.models.Portfolio;
import ru.kudryashov.userportfoliosservice.services.PortfolioService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PostMapping("/api/v1/users/{userId}/portfolios")
    public ResponseEntity<String> createNewPortfolioByUserId(@PathVariable String userId,
                                                             @RequestBody PortfolioDto portfolioDto) {
        return new ResponseEntity<>(
                portfolioService.createNewPortfolioByUserId(userId, portfolioDto),
                HttpStatus.CREATED);
    }

    @GetMapping("/api/v1/portfolios/{id}")
    public ResponseEntity<Portfolio> getPortfolioById(@PathVariable String id) {
        return ResponseEntity.ok(portfolioService.getPortfolioById(id));
    }

    @GetMapping("/api/v1/users/{userId}/portfolios")
    public ResponseEntity<List<Portfolio>> getAllPortfoliosByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(portfolioService.getAllPortfolioByUserId(userId));
    }

    @PostMapping("/api/v1/portfolios/{id}/positions")
    public ResponseEntity<String> createPositionInPortfolioById(@PathVariable String id,
                                                                @RequestBody PositionDto position) {
        return new ResponseEntity<>(
                portfolioService.createPositionInPortfolioById(id, position),
                HttpStatus.CREATED);
    }

    @PutMapping("/api/v1/portfolios/{id}/positions")
    public ResponseEntity<String> updatePositionInPortfolioById(@PathVariable String id,
                                                                @RequestBody PositionDto position) {
        return ResponseEntity.ok(portfolioService.updatePositionInPortfolioById(id, position));
    }
}
