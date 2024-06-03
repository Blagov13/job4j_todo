package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserStore {
    private final SessionFactory sf;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);

    public Optional<User> save(User user) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.of(user);
    }

    public Optional<User> findByLogin(String login) {
        var session = sf.openSession();
        Optional<User> user = Optional.empty();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery("from User i where i.login = :fLogin", User.class);
            query.setParameter("fLogin", login);
            user = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Ошибка при поиске пользователя по логину", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return user;
    }
}
