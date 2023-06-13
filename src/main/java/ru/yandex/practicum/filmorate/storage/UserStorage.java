package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ThrowableException;


import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User put(User user);

    List<User> getAllUsers();

    User getUser(final Integer id);

    boolean addFriend(int userId, int friendId) throws ThrowableException;

    boolean deleteFriend(int userId, int friendId) throws ThrowableException;


}
