package ru.yandex.practicum.filmorate.service;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User put(User user);

    List<User> getAllUsers();

    User getUser(Integer id);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    Collection<User> getFriends(Integer id);

    Collection<User> getCommonFriends(Integer id, Integer otherId);
}
