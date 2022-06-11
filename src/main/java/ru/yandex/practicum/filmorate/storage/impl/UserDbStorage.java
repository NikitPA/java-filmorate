package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDbStorage implements UserStorage {

    private final String getUserByIdQuery = "SELECT * FROM users WHERE user_id = ?";
    private final String getUsersQuery = "SELECT * FROM users";
    private final String updateUserQuery = "UPDATE users SET login = ?, name = ?,email = ?, birthday = ? where user_id = ? ";
    private final String deleteUserQuery = "DELETE FROM users WHERE user_id = ?";
    private final String findUsersLikeQuery =
            "SELECT * FROM users u JOIN likes l ON u.user_id = l.user_id WHERE film_id = ?";
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long save(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("users").
                usingGeneratedKeyColumns("user_id");
        return jdbcInsert.executeAndReturnKey(user.toMap()).intValue();
    }

    @Override
    public Collection<User> getUsers() {
        return jdbcTemplate.query(getUsersQuery, this::makeUser);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(updateUserQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                Date.valueOf(user.getBirthday()),
                user.getId());
    }

    @Override
    public void delete(Integer user_id) {
        jdbcTemplate.update(deleteUserQuery, user_id);
    }


    @Override
    public Optional<User> getUserById(Long id) {
        return jdbcTemplate.query(getUserByIdQuery,
                        this::makeUser, id).
                stream().findAny();
    }

    public Collection<User> findUsersLikeToFilm(Integer film_id) {
        return jdbcTemplate.query(findUsersLikeQuery, this::makeUser, film_id);

    }

    private User makeUser(ResultSet rs, int row) throws SQLException {
        return new User((long) rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
