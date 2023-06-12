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
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
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
    public boolean addLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.addLike(userId);
        put(film);
        return true;
    }

    @Override
    public boolean deleteLike(int filmId, int userId) {
        Film film = films.get(filmId);
        film.deleteLike(userId);
        put(film);
        return true;
    }

    @Override
    public Collection<Film> getMostPopularFilms(int size) {
        Collection<Film> mostPopularFilms = getAllFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(size)
                .collect(Collectors.toCollection(HashSet::new));
        return mostPopularFilms;
    }
}
