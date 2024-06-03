package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final SessionFactory sf;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskStore.class);

    public List<Task> findAll() {
        var session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public Optional<Task> findById(int id) {
        var session = sf.openSession();
        Task result = null;
        try {
            session.beginTransaction();
            result = session.get(Task.class, id);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return Optional.ofNullable(result);
    }

    public void save(Task task) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    public boolean update(Task task) {
        var session = sf.openSession();
        boolean isSuccess = false;
        try {
            session.beginTransaction();
            session.update(task);
            session.getTransaction().commit();
            isSuccess = true;
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isSuccess;
    }

    public boolean delete(int id) {
        var session = sf.openSession();
        boolean isSuccess = false;
        try {
            session.beginTransaction();
            session.delete(session.get(Task.class, id));
            session.getTransaction().commit();
            isSuccess = true;
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return isSuccess;
    }

    public List<Task> findCompleted() {
        var session = sf.openSession();
        List<Task> result = List.of();
        try {
            session.beginTransaction();
            result = session.createQuery("from Task as i where i.done = true", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            LOGGER.error("Во время транзакции произошла ошибка", e);
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
            LOGGER.error("Во время транзакции произошла ошибка", e);
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return result;
    }

    public boolean updateDone(int id, boolean done) {
        var session = sf.openSession();
        try {
            session.beginTransaction();
            var query = session.createQuery("update Task set done = :done where id = :id");
            query.setParameter("done", done);
            query.setParameter("id", id);
            int result = query.executeUpdate();
            session.getTransaction().commit();
            return result > 0;
        } catch (Exception e) {
            session.getTransaction().rollback();
            return false;
        } finally {
            session.close();
        }
    }
}
