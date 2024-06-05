package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.PriorityService;
import ru.job4j.todo.service.TaskService;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

@SessionAttributes("user")
@Controller
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService service;
    private final PriorityService priorityService;
    private final CategoryService categoryService;
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
        User user = (User) session.getAttribute("user");
        ZoneId userZoneId = getUserZoneId(user);
        Function<TaskService, List<Task>> strategy = filterStrategies.getOrDefault(filter, TaskService::findAll);
        List<Task> tasks = strategy.apply(service);
        adjustTaskTimes(tasks, userZoneId);
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/create";
    }

    @PostMapping
    public String create(@ModelAttribute Task task, HttpSession session, @RequestParam List<Integer> categoryIds) {
        User currentUser = (User) session.getAttribute("user");
        task.setUser(currentUser);
        task.setCategories(categoryService.findById(categoryIds));
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
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("categories", categoryService.findAll());
        return "tasks/edit";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable int id, @ModelAttribute Task task, @RequestParam List<Integer> categoryIds) {
        task.setId(id);
        task.setCategories(categoryService.findById(categoryIds));
        boolean isUpdate = service.update(task);
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

    private ZoneId getUserZoneId(User user) {
        if (user != null && user.getUserZone() != null) {
            return ZoneId.of(user.getUserZone().toLocalDateTime().toString());
        } else {
            return ZoneId.systemDefault();
        }
    }

    private void adjustTaskTimes(List<Task> tasks, ZoneId userZoneId) {
        for (Task task : tasks) {
            task.setCreated(convertToUserZone(task.getCreated(), userZoneId));
        }
    }

    private LocalDateTime convertToUserZone(LocalDateTime dateTime, ZoneId userZoneId) {
        ZonedDateTime systemZonedDateTime = dateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime userZonedDateTime = systemZonedDateTime.withZoneSameInstant(userZoneId);
        return userZonedDateTime.toLocalDateTime();
    }
}