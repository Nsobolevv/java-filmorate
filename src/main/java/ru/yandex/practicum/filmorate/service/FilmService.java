package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ThrowableException;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film createFilm(Film film);

    Film put(Film film);

    List<Film> getAllFilms();

    Film getFilm(Integer filmId);

    void addLike(Integer filmId, Integer userId) throws ThrowableException;

    void deleteLike(Integer filmId, Integer userId) throws ThrowableException;

    Collection<Film> getMostPopularFilms(Integer count);
}
