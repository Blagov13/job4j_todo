package ru.job4j.todo.store;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryStore {
    private final CrudStore crudStore;

    public List<Category> findAll() {
        return crudStore.query("from Category", Category.class);
    }

    public List<Category> findById(List<Integer> id) {
        return crudStore.query("from Category where id in (:fId)", Category.class, Map.of("fId", id));
    }
}
