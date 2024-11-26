package ru.kudryashov.userportfoliosservice.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kudryashov.userportfoliosservice.exceptions.EntityNotFoundException;
import ru.kudryashov.userportfoliosservice.models.Portfolio;
import ru.kudryashov.userportfoliosservice.models.User;
import ru.kudryashov.userportfoliosservice.repositories.UserRepository;
import ru.kudryashov.userportfoliosservice.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public String createNewUser(User user) {
        user.setPortfolios(List.of());
        var savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    public String addNewPortfolio(String userId, Portfolio portfolio) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id %s not found"
                        .formatted(userId)));

        var portfolios = user.getPortfolios();
        portfolios.add(portfolio);
        user.setPortfolios(portfolios);

        var updatedUser = userRepository.save(user);
        return updatedUser.getId();
    }
}
