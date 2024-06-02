package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;
    private static final Logger logger = LoggerFactory.getLogger(TaskStore.class);

    public List<Task> findAll() {
        var session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public Task findById(int id) {
        var session = sf.openSession();
        Task result = null;
        try {
            session.beginTransaction();
            result = session.get(Task.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public void save(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void update(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public void delete(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.delete(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public List<Task> findCompleted() {
        var session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task as i where i.done = true", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public List<Task> findNew() {
        var session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task as i where i.done = false", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }
}
