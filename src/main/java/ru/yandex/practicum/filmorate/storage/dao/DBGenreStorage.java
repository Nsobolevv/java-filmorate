package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import java.sql.*;
import java.util.List;

@Component
public class DBGenreStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void deleteFilmGenres(int filmId) {
        String deleteOldGenres = "delete from GENRELINE where FILMID = ?";
        jdbcTemplate.update(deleteOldGenres, filmId);
    }

    @Override
    public void addFilmGenres(int filmId, List<Genre> genres) {
        jdbcTemplate.batchUpdate("insert into GENRELINE (FILMID, GENREID) values (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setInt(1, filmId);
                preparedStatement.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRE.GENREID, NAME from GENRE " +
                "INNER JOIN GENRELINE GL on GENRE.GENREID = GL.GENREID " +
                "where FILMID = ?";
        return jdbcTemplate.query(sqlGenre, this::makeGenre, filmId);
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlGenre = "select GENREID, NAME from GENRE ORDER BY GENREID";
        return jdbcTemplate.query(sqlGenre, this::makeGenre);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from GENRE where GENREID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::makeGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " +
                    genreId + " не зарегистрирован!");
        }
        return genre;
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("GenreID"), resultSet.getString("Name"));
    }
}