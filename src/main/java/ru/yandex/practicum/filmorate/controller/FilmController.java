package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import ru.yandex.practicum.filmorate.model.Film;



import java.util.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;


    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film put(@RequestBody Film film) {
        return filmService.put(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilm(@PathVariable String id) {
        log.info("Получен запрос GET к эндпоинту: /films/{}", id);
        return filmService.getFilm(id);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> findMostPopular(@RequestParam(defaultValue = "10") String count) {
        log.info("Получен запрос GET к эндпоинту: /films/popular?count={}", count);
        return filmService.getMostPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос PUT к эндпоинту: /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, добавлен лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable String id, @PathVariable String userId) {
        log.info("Получен запрос DELETE к эндпоинту: films/{}/like/{}", id, userId);
        filmService.deleteLike(id, userId);
        log.info("Обновлен объект {} с идентификатором {}, удален лайк от пользователя {}",
                Film.class.getSimpleName(), id, userId);

    }

}
