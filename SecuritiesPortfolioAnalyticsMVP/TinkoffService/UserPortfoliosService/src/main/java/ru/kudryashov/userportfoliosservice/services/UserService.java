package ru.kudryashov.userportfoliosservice.services;

import ru.kudryashov.userportfoliosservice.models.Portfolio;
import ru.kudryashov.userportfoliosservice.models.User;

public interface UserService {

    String createNewUser(User user);

    String addNewPortfolio(String userId, Portfolio portfolio);
}
