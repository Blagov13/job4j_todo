package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.UserStore;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserStore store;

    public Optional<User> save(User user) {
        return store.save(user);
    }

    public Optional<User> findByLogin(String login) {
        return store.findByLogin(login);
    }
}
