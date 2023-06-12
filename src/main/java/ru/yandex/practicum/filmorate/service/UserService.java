package ru.yandex.practicum.filmorate.service;
import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.List;

public interface UserService {
    User createUser(User user);

    User put(User user);

    List<User> getAllUsers();

    User getUser(String id);

    void addFriend(String userId, String friendId);

    void deleteFriend(String userId, String friendId);

    Collection<User> getFriends(String id);

    Collection<User> getCommonFriends(String id, String otherId);
}
