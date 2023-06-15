package ru.yandex.practicum.filmorate.storage;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {
    User createUser(User user);

    User put(User user);

    List<User> getAllUsers();

    User getUser(Integer id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);


}
