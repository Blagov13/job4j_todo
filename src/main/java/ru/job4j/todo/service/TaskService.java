package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.store.TaskStore;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {
    private final TaskStore store;

    public List<Task> findAll() {
        return store.findAll();
    }

    public Task findById(int id) {
        return store.findById(id);
    }

    public void save(Task task) {
        store.save(task);
    }

    public void update(Task task) {
        store.update(task);
    }

    public void delete(Task task) {
        store.delete(task);
    }

    public List<Task> findCompleted() {
        return store.findCompleted();
    }

    public List<Task> findNew() {
        return store.findNew();
    }
}
