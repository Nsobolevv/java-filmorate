package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.service.InMemoryUserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest {
    InMemoryUserService inMemoryUserService;

    @BeforeEach
    void setUp() {
        inMemoryUserService = new InMemoryUserService(new InMemoryUserStorage());
    }

    @Test
    void createUserWhenUserNormal() {
        User user = inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        assertNotNull(inMemoryUserService.getAllUsers(), "User на возвращается.");
        assertEquals(1, inMemoryUserService.getAllUsers().size(), "Неверное количество.");
        assertEquals(1, user.getId(), "Неверное id.");
        assertEquals("aa@mail.ru", user.getEmail(), "Неверный email.");
        assertEquals("cole", user.getLogin(), "Неверный login.");
        assertEquals("cole", user.getName(), "Неверный name.");
        assertEquals(LocalDate.of(1997, 12, 28), user.getBirthday(), "Неверный birthday.");
    }

    @Test
    void createUserWhenUserFailLoginEmailBirthday() {
        assertThrows(ValidationException.class,() -> inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole cole","niko", LocalDate.of(1997, 12, 28))));
        assertThrows(ValidationException.class,() -> inMemoryUserService.createUser(new User(null,"mail.ru","cole","niko", LocalDate.of(1997, 12, 28))));
        assertThrows(ValidationException.class,() -> inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole","niko", LocalDate.of(2045, 12, 28))));
    }

    @Test
    void putUserWhenUserNormal() {
        User user = inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));;
        assertEquals("cole", inMemoryUserService.getAllUsers().get(0).getName(), "Неверный name.");
        User userUpdate = inMemoryUserService.put(new User(1,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28)));
        assertEquals("niko", inMemoryUserService.getAllUsers().get(0).getName(), "Неверный name.");
        assertNotEquals(user,userUpdate,"Users совпадают.");
    }

    @Test
    void putUserWhenId9999() {
        User user = inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        assertThrows(NotFoundException.class,() -> inMemoryUserService.put(new User(9999,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28))));
    }

    @Test
    void getAllUsers() {
        User user = inMemoryUserService.createUser(new User(null,"aa@mail.ru","cole",null, LocalDate.of(1997, 12, 28)));
        User user1 = inMemoryUserService.createUser(new User(1,"aa@mail.ru","cole","niko",LocalDate.of(1997, 12, 28)));
        assertNotNull(inMemoryUserService.getAllUsers(), "User на возвращается.");
        assertEquals(user1,inMemoryUserService.getAllUsers().get(1),"User не совпадает.");
    }
}