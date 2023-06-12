package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmService {
    Film createFilm(Film film);

    Film put(Film film);

    List<Film> getAllFilms();

    Film getFilm(String filmId);

    void addLike(String filmId, String userId);

    void deleteLike(String filmId, String userId);

    Collection<Film> getMostPopularFilms(String count);
}
