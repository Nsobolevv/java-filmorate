package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import java.util.*;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private Integer generatedId = 1;

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            User newUser = new User(generatedId++,user.getEmail(),user.getLogin(),user.getLogin(),user.getBirthday());
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            user.setId(generatedId++);
            user.setFriends(new HashSet<>());
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public User put(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    user.getId() + " не зарегистрирован!");
        }

        users.remove(user.getId());
        if (user.getName() == null || user.getName().isBlank()) {
            User newUser = new User(user.getId(),user.getEmail(),user.getLogin(),user.getLogin(),user.getBirthday(),user.getFriends());
            users.put(newUser.getId(), newUser);
            return newUser;
        } else {
            users.put(user.getId(), user);
            return user;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Integer id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);

    }

}
