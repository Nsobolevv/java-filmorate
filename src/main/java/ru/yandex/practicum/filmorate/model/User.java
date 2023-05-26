package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDate;


@Data
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    @EqualsAndHashCode.Exclude
    private Integer id;
    private final String email;
    private final String login;
    private final String name;
    private final LocalDate birthday;
}
