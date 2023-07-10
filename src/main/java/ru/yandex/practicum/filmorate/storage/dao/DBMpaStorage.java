package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class DBMpaStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBMpaStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Mpa> getAllMpa() {
        String sqlMpa = "select * from RATINGMPA";
        return jdbcTemplate.query(sqlMpa, (rs, rowNum) -> makeMpa(rs));
    }

    private Mpa makeMpa(ResultSet resultSet) throws SQLException {
        return new Mpa(resultSet.getInt("RatingID"),
                resultSet.getString("Name"),
                resultSet.getString("Description"));
    }

    @Override
    public Mpa getMpaById(int mpaId) {
        String sqlMpa = "select * from RATINGMPA where RATINGID = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlMpa, (rs, rowNum) -> makeMpa(rs), mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Возрастной рейтинг с идентификатором " +
                    mpaId + " не зарегистрирован!");
        }
        return mpa;
    }
}