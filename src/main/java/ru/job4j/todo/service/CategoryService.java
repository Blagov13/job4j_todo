package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.CategoryStore;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryStore store;

    public List<Category> findAll() {
        return store.findAll();
    }

    public List<Category> findById(List<Integer> id) {
        return store.findById(id);
    }
}