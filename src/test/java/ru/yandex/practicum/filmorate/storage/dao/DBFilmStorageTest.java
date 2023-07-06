package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class DBFilmStorageTest {

    private final DBFilmStorage filmStorage;

    @Test
    public void testGetFilmById() {

        Film createFilm = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90,
                3,
                new Mpa(1,"o","o"),
                new ArrayList<>(),
                new ArrayList<>());
        filmStorage.createFilm(createFilm);

        Film dbFilm = filmStorage.getFilm(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllFilms() {
        Film first = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90,
                3,
                new Mpa(1,"o","o"),
                new ArrayList<>(),
                new ArrayList<>());
        Film second = new Film(2,
                "second",
                "second description",
                LocalDate.now().minusYears(15),
                100,
                2,
                new Mpa(3,"o","o"),
                new ArrayList<>(),
                new ArrayList<>());
        filmStorage.createFilm(first);
        filmStorage.createFilm(second);

        Collection<Film> dbFilms = filmStorage.getAllFilms();
        assertEquals(2, dbFilms.size());
    }

    @Test
    void updateFilm() {
        Film first = new Film(1,
                "first",
                "first description",
                LocalDate.now().minusYears(8),
                90,
                3,
                new Mpa(1,"o","o"),
                new ArrayList<>(),
                new ArrayList<>());
        Film added = filmStorage.createFilm(first);
        added.setName("update");
        filmStorage.put(added);
        Film dbFilm = filmStorage.getFilm(added.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "update");
    }
}