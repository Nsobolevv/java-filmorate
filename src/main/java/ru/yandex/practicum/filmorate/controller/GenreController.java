package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Collection<Genre> findAll() {
        log.info("Получен запрос GET к эндпоинту: /genres");
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public Genre findGenre(@PathVariable Integer id) {
        log.info("Получен запрос GET к эндпоинту: /genres/{}", id);
        return genreService.getGenre(id);
    }
}