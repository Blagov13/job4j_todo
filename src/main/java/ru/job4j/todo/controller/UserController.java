package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.todo.config.TimeZoneWrapper;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.TimeZone;

@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    /*Метод для загрузки списка поддерживаемых часовых поясов*/
    private ArrayList<TimeZoneWrapper> getSupportedTimezones() {
        ArrayList<TimeZoneWrapper> timezones = new ArrayList<>();
        for (String timeId : TimeZone.getAvailableIDs()) {
            TimeZone timeZone = TimeZone.getTimeZone(timeId);
            timezones.add(new TimeZoneWrapper(timeId, timeZone.getDisplayName()));
        }
        return timezones;
    }

    @GetMapping({"/", "/register"})
    public String getRegistrationPage(Model model, HttpSession session) {
        model.addAttribute("user", new User());
        ArrayList<TimeZoneWrapper> timezones = getSupportedTimezones();
        model.addAttribute("timezones", timezones);
        return "users/registration";
    }

    @PostMapping("/register")
    public String register(Model model, @ModelAttribute User user) {
        var savedUser = service.save(user);
        if (savedUser.isEmpty()) {
            model.addAttribute("error", "Пользователь с таким логином уже существует.");
            return "/users/registration";
        }
        return "redirect:/tasks";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, HttpSession session) {
        return "users/login";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute User user, Model model, HttpServletRequest request) {
        var userOptional = service.findByLogin(user.getLogin());
        if (userOptional.isEmpty()) {
            model.addAttribute("error", "Почта или пароль введены неверно");
            return "users/login";
        }
        var session = request.getSession();
        session.setAttribute("user", userOptional.get());
        return "redirect:/tasks";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }
}
