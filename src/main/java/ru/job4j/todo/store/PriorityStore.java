package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class PriorityStore {
    private final CrudStore crudStore;

    public List<Priority> findAll() {
        return crudStore.query("from Priority", Priority.class);
    }

    public Optional<Priority> findById(int id) {
        return crudStore.optional("from Priority where id = :fId", Priority.class, Map.of("fId", id));
    }
}
