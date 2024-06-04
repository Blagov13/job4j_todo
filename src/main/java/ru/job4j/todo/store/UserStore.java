package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore {
    private final CrudStore crudStore;

    public Optional<User> save(User user) {
        return crudStore.tx(session -> {
            session.save(user);
            return Optional.of(user);
        });
    }

    public Optional<User> findByLogin(String login) {
        return crudStore.optional("from User where login = :fLogin", User.class, Map.of("fLogin", login));
    }
}
