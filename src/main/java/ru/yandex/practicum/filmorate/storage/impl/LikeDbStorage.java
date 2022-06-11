package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Repository
public class LikeDbStorage implements LikeStorage {

    private final String saveLike = "insert into likes(film_id, user_id) values (?, ?)";
    private final String deleteLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public LikeDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Long film_id, Long user_id) {
        jdbcTemplate.update(saveLike, film_id, user_id);
    }

    @Override
    public void delete(Long film_id, Long user_id) {
        jdbcTemplate.update(deleteLike, film_id, user_id);
    }
}
