package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);

    Film put(Film film);

    List<Film> getAllFilms();

    Film getFilm(int filmId);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getMostPopularFilms(int count);

}
