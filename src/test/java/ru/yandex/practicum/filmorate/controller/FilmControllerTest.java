package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void createFilmWhenFilmNormal() {
        Film film = filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1980, 05, 23),144));
        assertNotNull(filmController.getAllFilms(), "Film на возвращается.");
        assertEquals(1, filmController.getAllFilms().size(), "Неверное количество.");
        assertEquals(1, film.getId(), "Неверное id.");
        assertEquals("The Shining", film.getName(), "Неверный name.");
        assertEquals("description", film.getDescription(), "Неверный description.");
        assertEquals(LocalDate.of(1980, 05, 23), film.getReleaseDate(), "Неверный releaseDate.");
        assertEquals(144, film.getDuration(), "Неверный duration.");
    }

    @Test
    void createFilmWhenFailNameDescriptionReleaseDateDuration() {
        assertThrows(ValidationException.class,() -> filmController.createFilm(new Film(null,"","description", LocalDate.of(1980, 05, 23),144)));
        assertThrows(ValidationException.class,() -> filmController.createFilm(new Film(null,"The Shining","Джек Торренс" +
                " с женой и сыном приезжает в элегантный отдалённый отель, чтобы работать смотрителем во время мертвого сезона. " +
                "Торренс здесь раньше никогда не бывал. Или это не совсем так? Ответ лежит во мраке, " +
                "сотканном из преступного кошмара.", LocalDate.of(1980, 05, 23),144)));
        assertThrows(ValidationException.class,() -> filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1880, 05, 23),144)));
        assertThrows(ValidationException.class,() -> filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1980, 05, 23),-144)));
    }

    @Test
    void putFilmWhenFilmNormal() {
        Film film = filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1980, 05, 23),144));
        assertEquals(film, filmController.getAllFilms().get(0), "Неверный film.");
        Film filmUpdate = filmController.put(new Film(1,"Inception","descriptionInception", LocalDate.of(2010, 07, 13),148));
        assertEquals(filmUpdate, filmController.getAllFilms().get(0), "Неверный film.");
        assertNotEquals(film,filmUpdate,"Users совпадают.");
    }

    @Test
    void putFilmWhenId9999() {
        Film film = filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1980, 05, 23),144));
        assertThrows(ValidationException.class,() -> filmController.put(new Film(9999,"The Shining","description", LocalDate.of(1980, 05, 23),144)));
    }

    @Test
    void getAllFilms() {
        Film film = filmController.createFilm(new Film(null,"The Shining","description", LocalDate.of(1980, 05, 23),144));
        Film film1 = filmController.createFilm(new Film(1,"Inception","descriptionInception", LocalDate.of(2010, 07, 13),148));
        assertNotNull(filmController.getAllFilms(), "Film на возвращается.");
        assertEquals(film1,filmController.getAllFilms().get(1),"Film не совпадает.");
    }
}