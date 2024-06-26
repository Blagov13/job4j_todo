package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskStore {
    private final CrudStore crudStore;

    public List<Task> findAll() {
        return crudStore.query("from Task", Task.class);
    }

    public Optional<Task> findById(int id) {
        return crudStore.optional("from Task where id = :fId", Task.class, Map.of("fId", id));
    }

    public void save(Task task) {
        crudStore.run(session -> session.save(task));
    }

    public boolean update(Task task) {
        String query = "update Task set title = :title, description = :description,"
                + "created = :created, done = :done, user = :user, priority = :priority, categories = :categories"
                + " where id = :id";
        Map<String, Object> params = Map.of(
                "name", task.getTitle(),
                "description", task.getDescription(),
                "created", task.getCreated(),
                "done", task.isDone(),
                "user", task.getUser(),
                "priority", task.getPriority(),
                "categories", task.getCategories(),
                "id", task.getId()
        );
        return crudStore.runWithResult(query, params);
    }

    public boolean delete(int id) {
        String query = "delete from Task where id = :fId";
        Map<String, Object> params = Map.of("fId", id);
        return crudStore.runWithResult(query, params);
    }

    public List<Task> findCompleted() {
        return crudStore.query("from Task as i where i.done = true", Task.class);
    }

    public List<Task> findNew() {
        return crudStore.query("from Task as i where i.done = false", Task.class);
    }

    public boolean updateDone(int id, boolean done) {
        try {
            crudStore.run("update Task set done = :fDone where id = :fId", Map.of("fDone", done, "fId", id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
