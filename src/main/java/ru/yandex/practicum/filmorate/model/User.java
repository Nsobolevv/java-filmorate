package ru.yandex.practicum.filmorate.model;

import lombok.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    @EqualsAndHashCode.Exclude
    private Integer id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();

    public User(Integer id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public boolean addFriend(final Integer id) {
        return friends.add(id);
    }

    public boolean deleteFriend(final Integer id) {
        return friends.remove(id);
    }
}
