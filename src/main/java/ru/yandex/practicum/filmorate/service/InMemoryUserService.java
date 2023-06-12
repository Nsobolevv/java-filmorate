package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.WrongIdException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
public class InMemoryUserService implements UserService {
    private final UserStorage userStorage;

    public InMemoryUserService(UserStorage userStorage) {
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
    public User getUser(String id) {
        return getStoredUser(id);
    }

    @Override
    public void addFriend(String userId, String friendId) {
        User user = getStoredUser(userId);
        User friend = getStoredUser(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
    }

    @Override
    public void deleteFriend(String userId, String friendId) {
        User user = getStoredUser(userId);
        User friend = getStoredUser(friendId);
        userStorage.deleteFriend(user.getId(), friend.getId());
    }

    @Override
    public Collection<User> getFriends(String userId) {
        User user = getStoredUser(userId);
        Collection<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.getUser(id));
        }
        return friends;
    }

    @Override
    public Collection<User> getCommonFriends(String userId, String otherId) {
        User user = getStoredUser(userId);
        User otherUser = getStoredUser(otherId);
        Collection<User> commonFriends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                commonFriends.add(userStorage.getUser(id));
            }
        }
        return commonFriends;
    }

    private Integer idFromString(final String id) {
        try {
            return Integer.valueOf(id);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private User getStoredUser(final String id) {
        final int userId = idFromString(id);
        if (userId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор пользователя: " +
                    "значение " + id);
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован!");
        }
        return user;
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
