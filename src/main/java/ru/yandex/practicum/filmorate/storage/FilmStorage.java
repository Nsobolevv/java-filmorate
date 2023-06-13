package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ThrowableException;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film put(Film film);

    List<Film> getAllFilms();

    Film getFilm(int filmId);

    boolean addLike(int filmId, int userId) throws ThrowableException;

    boolean deleteLike(int filmId, int userId) throws ThrowableException;

    Collection<Film> getMostPopularFilms(int count);

}
