package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.UserService;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ThrowableException;


import java.util.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос POST к эндпоинту: /users/ Данные тела запроса: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        log.info("Получен запрос PUT к эндпоинту: /users/ Данные тела запроса: {}", user);
        return userService.put(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос GET к эндпоинту: /users/");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/", id);
        return userService.getUser(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws ThrowableException {
        log.info("Получен запрос PUT к эндпоинту: /users/{}/friends/{}", id, friendId);
        //final User validUser = userService.update(user);
        userService.addFriend(id,friendId);
        log.info("Обновлен объект {} с идентификатором {}. Добавлен друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) throws ThrowableException {
        log.info("Получен запрос DELETE к эндпоинту: /users/{}/friends/{}", id, friendId);
        userService.deleteFriend(id,friendId);
        log.info("Обновлен объект {} с идентификатором {}. Удален друг {}",
                User.class.getSimpleName(), id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findFriends(@PathVariable("id") Integer id) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        log.info("Получен запрос GET к эндпоинту: /users/{}/friends/common/{}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }




}
