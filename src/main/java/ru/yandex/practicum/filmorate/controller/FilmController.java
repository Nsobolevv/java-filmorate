package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer generatedId = 1;
    private static final LocalDate dateRestriction = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        isValid(film);
        film.setId(generatedId++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            throw new ValidationException("такого id не сущетствует");
        }
        isValid(film);
        films.remove(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());

    }

    private void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("название не может быть пустым");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        } else if (film.getReleaseDate().isBefore(dateRestriction)) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
