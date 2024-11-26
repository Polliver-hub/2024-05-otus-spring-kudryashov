package ru.kudryashov.userportfoliosservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kudryashov.userportfoliosservice.models.User;
import ru.kudryashov.userportfoliosservice.repositories.UserRepository;
import ru.kudryashov.userportfoliosservice.services.UserService;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ResponseEntity<String> addNewUser(User user) {
        return ResponseEntity.ok(userService.createNewUser(user));
    }

    @GetMapping("/api/v1/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        var user = userRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(user);
    }

}
