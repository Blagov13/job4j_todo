package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskStore store;

    public List<Task> findAll() {
        return store.findAll();
    }

    public Optional<Task> findById(int id) {
        return store.findById(id);
    }

    public void save(Task task) {
        store.save(task);
    }

    public boolean update(Task task) {
        return store.update(task);
    }

    public boolean delete(int id) {
        return store.delete(id);
    }

    public List<Task> findCompleted() {
        return store.findCompleted();
    }

    public List<Task> findNew() {
        return store.findNew();
    }

    public boolean updateDone(int id, boolean done) {
        return store.updateDone(id, done);
    }
}
