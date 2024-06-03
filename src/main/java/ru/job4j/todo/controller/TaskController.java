package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;
    private Map<String, Function<TaskService, List<Task>>> filterStrategies;

    @PostConstruct
    public void init() {
        filterStrategies = new HashMap<>();
        filterStrategies.put("all", TaskService::findAll);
        filterStrategies.put("completed", TaskService::findCompleted);
        filterStrategies.put("new", TaskService::findNew);
        /*Здесь можно добавить еще критерии*/
    }

    @GetMapping
    public String list(@RequestParam(required = false, defaultValue = "all") String filter, Model model, HttpSession session) {
        Function<TaskService, List<Task>> strategy = filterStrategies.getOrDefault(filter, TaskService::findAll);
        List<Task> tasks = strategy.apply(service);
        model.addAttribute("tasks", tasks);
        model.addAttribute("filter", filter);
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String view(@PathVariable int id, Model model) {
        var task = service.findById(id);
        if (task.isEmpty()) {
            return "errors/404";
        }
        Task task1 = task.get();
        model.addAttribute("task", task1);
        return "tasks/view";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping
    public String create(@ModelAttribute Task task) {
        service.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        var taskOptional = service.findById(id);
        if (taskOptional.isEmpty()) {
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable int id, @ModelAttribute Task task) {
        Task existingTask = service.findById(id).orElse(null);
        if (existingTask == null) {
            return "errors/404";
        }
        task.setId(existingTask.getId());
        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDone(task.isDone());
        boolean isUpdate = service.update(existingTask);
        if (!isUpdate) {
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id) {
        boolean isDeleted = service.delete(id);
        if (!isDeleted) {
            return "errors/404";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/{id}/complete")
    public String complete(@PathVariable int id) {
        boolean isUpdated = service.updateDone(id, true);
        if (!isUpdated) {
            return "errors/404";
        }
        return "redirect:/tasks";
    }
}
