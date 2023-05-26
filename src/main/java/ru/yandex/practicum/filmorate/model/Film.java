package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;


@Data
@EqualsAndHashCode
@AllArgsConstructor
public class Film {
    @EqualsAndHashCode.Exclude
    private Integer id;
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
