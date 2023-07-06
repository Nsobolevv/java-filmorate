package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private Integer generatedId = 1;

    @Override
    public Film createFilm(Film film) {
        film.setId(generatedId++);
        film.setLikes(new ArrayList<>());
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
        Film film = films.get(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.deleteLike(userId);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int size) {
        return getAllFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(size)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
