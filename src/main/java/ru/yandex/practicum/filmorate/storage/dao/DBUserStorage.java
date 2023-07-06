package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.validation.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component("DBUserStorage")
public class DBUserStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DBUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUser(Integer id) {
        String sqlUser = "select * from USERS where USERID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> makeUser(rs), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    id + " не зарегистрирован!");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlAllUsers = "select * from USERS";
        return jdbcTemplate.query(sqlAllUsers, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("USERS")
                .usingGeneratedKeyColumns("UserID");
        if (user.getName() == null || user.getName().isBlank()) {
            Map<String, String> params = Map.of("Login", user.getLogin(),"Name", user.getLogin(), "Email", user.getEmail(),"Birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            Number id = simpleJdbcInsert.executeAndReturnKey(params);
            user.setId(id.intValue());
            return new User(id.intValue(),user.getEmail(),user.getLogin(),user.getLogin(),user.getBirthday());
        } else {
            Map<String, String> params = Map.of("Login", user.getLogin(),"Name", user.getName(), "Email", user.getEmail(),"Birthday", user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            Number id = simpleJdbcInsert.executeAndReturnKey(params);
            user.setId(id.intValue());
            return user;
        }
    }

    @Override
    public User put(User user) {
        String sqlUser = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USERID = ?";
        jdbcTemplate.update(sqlUser,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());

        return getUser(user.getId());
    }

    private User makeUser(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        return new User(
                userId,
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                resultSet.getString("Name"),
                Objects.requireNonNull(resultSet.getDate("BirthDay")).toLocalDate(),
                getUserFriends(userId));
    }

    private List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "select FRIENDID from FRIENDSHIP where USERID = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from FRIENDSHIP " +
                "where USERID = ? and FRIENDID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = sqlRowSet.next();
        String sqlSetFriend = "insert into FRIENDSHIP (USERID, FRIENDID, STATUS) " +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "update FRIENDSHIP set STATUS = true " +
                    "where USERID = ? and FRIENDID = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "delete from FRIENDSHIP where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update FRIENDSHIP set STATUS = false " +
                "where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
    }

}