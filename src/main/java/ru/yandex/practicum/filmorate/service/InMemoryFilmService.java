package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.validation.ValidationException;
import ru.yandex.practicum.filmorate.validation.WrongIdException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class InMemoryFilmService implements FilmService {
    private static final LocalDate dateRestriction = LocalDate.of(1895, 12, 28);

    private final FilmStorage filmStorage;
    private final UserService userService;

    public InMemoryFilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }


    @Override
    public Film createFilm(Film film) {
        isValid(film);
        return filmStorage.createFilm(film);
    }

    @Override
    public Film put(Film film) {
        isValid(film);
        return filmStorage.put(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilm(String filmId) {
        return getStoredFilm(filmId);
    }


    @Override
    public void addLike(String id, String userId) {
        Film film = getStoredFilm(id);
        User user = userService.getUser(userId);
        filmStorage.addLike(film.getId(), user.getId());
    }

    @Override
    public void deleteLike(String id, String userId) {
        Film film = getStoredFilm(id);
        User user = userService.getUser(userId);
        filmStorage.deleteLike(film.getId(), user.getId());
    }

    @Override
    public Collection<Film> getMostPopularFilms(String count) {
        Integer size = intFromString(count);
        if (size == Integer.MIN_VALUE) {
            size = 10;
        }
        return filmStorage.getMostPopularFilms(size);
    }

    private Integer intFromString(final String supposedInt) {
        try {
            return Integer.valueOf(supposedInt);
        } catch (NumberFormatException exception) {
            return Integer.MIN_VALUE;
        }
    }

    private Film getStoredFilm(final String supposedId) {
        final int filmId = intFromString(supposedId);
        if (filmId == Integer.MIN_VALUE) {
            throw new WrongIdException("Не удалось распознать идентификатор фильма: " +
                    "значение " + supposedId);
        }
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        return film;
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
