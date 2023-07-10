package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service

public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public InMemoryUserService(@Qualifier("DBUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
        isValid(user);
        return userStorage.createUser(user);
    }

    @Override
    public User put(User user) {
        isValid(user);
        return userStorage.put(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    @Override
    public Collection<User> getFriends(Integer userId) {
        User user = getUser(userId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(Integer userId, Integer otherId) {
        User user = getUser(userId);
        User otherUser = getUser(otherId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }

    private void isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может быть пустым и содержать пробелы");
        }
        if (LocalDate.now().isBefore(user.getBirthday())) {
            throw new ValidationException("дата рождения не может быть в будущем");
        }
    }
}
