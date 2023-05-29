package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void createUserWhenUserNormal() {
        User user = userController.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        assertNotNull(userController.getAllUsers(), "User на возвращается.");
        assertEquals(1, userController.getAllUsers().size(), "Неверное количество.");
        assertEquals(1, user.getId(), "Неверное id.");
        assertEquals("aa@mail.ru", user.getEmail(), "Неверный email.");
        assertEquals("cole", user.getLogin(), "Неверный login.");
        assertEquals("cole", user.getName(), "Неверный name.");
        assertEquals(LocalDate.of(1997, 12, 28), user.getBirthday(), "Неверный birthday.");
    }

    @Test
    void createUserWhenUserFailLoginEmailBirthday() {
        assertThrows(ValidationException.class,() -> userController.createUser(new User(null,"aa@mail.ru","cole cole","niko", LocalDate.of(1997, 12, 28))));
        assertThrows(ValidationException.class,() -> userController.createUser(new User(null,"mail.ru","cole","niko", LocalDate.of(1997, 12, 28))));
        assertThrows(ValidationException.class,() -> userController.createUser(new User(null,"aa@mail.ru","cole","niko", LocalDate.of(2045, 12, 28))));
    }

    @Test
    void putUserWhenUserNormal() {
        User user = userController.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));;
        assertEquals("cole", userController.getAllUsers().get(0).getName(), "Неверный name.");
        User userUpdate = userController.put(new User(1,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28)));
        assertEquals("niko", userController.getAllUsers().get(0).getName(), "Неверный name.");
        assertNotEquals(user,userUpdate,"Users совпадают.");
    }

    @Test
    void putUserWhenId9999() {
        User user = userController.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        assertThrows(ValidationException.class,() -> userController.put(new User(9999,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28))));
    }

    @Test
    void getAllUsers() {
        User user = userController.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        User user1 = userController.createUser(new User(1,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28)));
        assertNotNull(userController.getAllUsers(), "User на возвращается.");
        assertEquals(user1,userController.getAllUsers().get(1),"User не совпадает.");
    }
}