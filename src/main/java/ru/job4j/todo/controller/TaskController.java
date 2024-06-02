package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "all") String filter, Model model) {
        List<Task> tasks;
        switch (filter) {
            case "completed":
                tasks = service.findCompleted();
                break;
            case "new":
                tasks = service.findNew();
                break;
            default:
                tasks = service.findAll();
                break;
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable int id, Model model) {
        model.addAttribute("task", service.findById(id));
        return "tasks/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping
    public String create(@ModelAttribute Task task) {
        task.setCreated(LocalDateTime.now());
        service.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        model.addAttribute("task", service.findById(id));
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable int id, @ModelAttribute Task task) {
        task.setId(id);
        service.update(task);
        return "redirect:/tasks";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        service.delete(service.findById(id));
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable int id) {
        Task task = service.findById(id);
        task.setDone(true);
        service.update(task);
        return "redirect:/tasks";
    }
}
