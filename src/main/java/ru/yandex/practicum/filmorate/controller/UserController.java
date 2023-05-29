package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer generatedId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        isValid(user);
       if (user.getName() == null || user.getName().isBlank()) {
           User newUser = new User(generatedId++,user.getEmail(),user.getLogin(),user.getLogin(),user.getBirthday());
           users.put(newUser.getId(), newUser);
           return newUser;
       } else {
            user.setId(generatedId++);
            users.put(user.getId(), user);
            return user;
        }
    }

    @PutMapping
    public User put(@RequestBody User user) {
        if (users.get(user.getId()) == null) {
            throw new ValidationException("такого id не существует");
        }
        isValid(user);
        users.remove(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            User newUser = new User(user.getId(),user.getEmail(),user.getLogin(),user.getLogin(),user.getBirthday());
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            users.put(user.getId(), user);
            return user;
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    private void isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        } else if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        } else if (LocalDate.now().isBefore(user.getBirthday())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }

}
