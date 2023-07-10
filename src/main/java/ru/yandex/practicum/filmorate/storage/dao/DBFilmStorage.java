package ru.yandex.practicum.filmorate.storage.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("DBFilmStorage")
public class DBFilmStorage implements FilmStorage {

    private final Logger log = LoggerFactory.getLogger(DBFilmStorage.class);
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;

    public DBFilmStorage(JdbcTemplate jdbcTemplate, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("FILM")
                .usingGeneratedKeyColumns("FilmID");
    }

    @Override
    public Film getFilm(int filmId) {

        String sqlFilm = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID " +
                "where FILMID = ?";
        try {
            Film film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> makeFilm(rs), filmId);
            log.info("Найден фильм: {} {}", film.getId(), film.getName());
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.RATINGID = R.RATINGID ";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> makeFilm(resultSet));
    }

    @Override
    public Film createFilm(Film film) {
            Map<String, String> params = Map.of(
                    "Name", film.getName(),
                    "Description", film.getDescription(),
                    "ReleaseDate", film.getReleaseDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    "Duration", Integer.toString(film.getDuration()),
                    "Rate",Integer.toString(film.getRate()),
                    "RatingID",Integer.toString(film.getMpa().getId()));
            Number id = simpleJdbcInsert.executeAndReturnKey(params);
        film.setId(id.intValue());

        if (!film.getGenres().isEmpty()) {
            genreStorage.addFilmGenres(film.getId(), genres(film.getGenres()));
        }
            return film;
    }

    @Override
    public Film put(Film film) {
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ? ,RATINGID = ? " +
                "where FILMID = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        genreStorage.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreStorage.addFilmGenres(film.getId(), genres(film.getGenres()));
        }
        return getFilm(film.getId());
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "select * from LIKES where USERID = ? and FILMID = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into LIKES (USERID, FILMID) values (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String deleteLike = "delete from LIKES where FILMID = ? and USERID = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
    }

    @Override
    public Collection<Film> getMostPopularFilms(int count) {
        String sqlMostPopular = "select count(L.LIKEID) as likeRate" +
                ",FILM.FILMID" +
                ",FILM.NAME ,FILM.DESCRIPTION ,RELEASEDATE ,DURATION ,RATE ,R.RATINGID, R.NAME, R.DESCRIPTION from FILM " +
                "left join LIKES L on L.FILMID = FILM.FILMID " +
                "inner join RATINGMPA R on R.RATINGID = FILM.RATINGID " +
                "group by FILM.FILMID " +
                "ORDER BY likeRate desc " +
                "limit ?";
        return jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> makeFilm(rs), count);
    }

    private Film makeFilm(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("FilmID");
        return new Film(
                filmId,
                resultSet.getString("Name"),
                resultSet.getString("Description"),
                Objects.requireNonNull(resultSet.getDate("ReleaseDate")).toLocalDate(),
                resultSet.getInt("Duration"),
                resultSet.getInt("Rate"),
                new Mpa(resultSet.getInt("RatingMPA.RatingID"),
                        resultSet.getString("RatingMPA.Name"),
                        resultSet.getString("RatingMPA.Description")),
                genreStorage.getGenresByFilmId(filmId),
                getFilmLikes(filmId));
    }

    private List<Integer> getFilmLikes(int filmId) {
        String sqlGetLikes = "select USERID from LIKES where FILMID = ?";
        return jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
    }

    private List<Genre> genres(List<Genre> listGenre) {
        List<Genre> genreList = new ArrayList<>();
        List<Integer> genreId = new ArrayList<>();
        for (Genre genre : listGenre) {
            if (!genreId.contains(genre.getId())) {
                genreId.add(genre.getId());
                genreList.add(genre);
            }
        }
        return genreList;
    }
}
