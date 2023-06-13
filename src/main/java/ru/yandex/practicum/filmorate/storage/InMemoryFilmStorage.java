package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ThrowableException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private Integer generatedId = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(generatedId++);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film put(Film film) {
        if (films.get(film.getId()) == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    film.getId() + " не зарегистрирован!");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int filmId) {
        return films.get(filmId);
    }

    @Override
    public boolean addLike(int filmId, int userId) throws ThrowableException {
        Film film = films.get(filmId);
        film.addLike(userId);
        if (film.getLikes().contains(userId)) {
            put(film);
            return true;
        } else {
            throw new ThrowableException("Не получилось добавить like фильму:" + film.getName());
        }
    }

    @Override
    public boolean deleteLike(int filmId, int userId) throws ThrowableException {
        Film film = films.get(filmId);
        film.deleteLike(userId);
        if (!film.getLikes().contains(userId)) {
            put(film);
            return true;
        } else {
            throw new ThrowableException("Не получилось удалить like у фильма:" + film.getName());
        }
    }

    @Override
    public Collection<Film> getMostPopularFilms(int size) {
        return getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(size)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
