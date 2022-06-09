package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.stream.Collectors;

@Repository
public class FriendshipDbStorage implements FriendshipStorage {

    private final String saveFriendshipQuery =
            "INSERT INTO Friendships (user_id , friend_id , is_agree) VALUES (? , ? , ?)";
    private final String isAgreeQuery = "SELECT * FROM Friendships WHERE user_id = ? AND friend_id = ?";
    private final String updateIsAgreeQuery =
            "UPDATE Friendships SET is_agree = ? WHERE user_id = ? AND friend_id = ?";
    private final String deleteFriendship = "DELETE FROM Friendships WHERE user_id = ? AND friend_id = ?";
    private final String getUserFriendsQuery =
            "SELECT * FROM Users u JOIN Friendships f ON u.user_id = f.friend_id WHERE f.user_id = ? ";
    //!!!!!!!!!!!!!!
    private final JdbcTemplate jdbcTemplate;

    public FriendshipDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(long user_id, long friend_id) {
        //Нужно еще учесть, что запись уже с true
        if (jdbcTemplate.queryForRowSet(isAgreeQuery, user_id, friend_id).next()) {
            jdbcTemplate.update(updateIsAgreeQuery, true, user_id, friend_id);
        } else {
            jdbcTemplate.update(saveFriendshipQuery, user_id, friend_id, false);
        }
    }

    @Override
    public void delete(long user_id, long friend_id) {
        if (jdbcTemplate.queryForRowSet(isAgreeQuery, user_id, friend_id).next()) {
            if (jdbcTemplate.query(isAgreeQuery, this::getIsAgree, user_id, friend_id).
                    stream().findAny().orElseThrow()) {
                jdbcTemplate.update(updateIsAgreeQuery,
                        false,
                        friend_id,
                        user_id);
            } else {
                jdbcTemplate.update(deleteFriendship,
                        user_id,
                        friend_id);
            }
        }
    }

    @Override
    public Collection<User> getFriends(long id) {
        return jdbcTemplate.query(getUserFriendsQuery,
                this::makeUser, id);
    }

    @Override
    public Collection<User> getCommonFriends(long user_id, long user2_id) {
        return getFriends(user_id).stream()
                .distinct()
                .filter(user1 -> getFriends(user2_id).contains(user1))
                .collect(Collectors.toSet());
    }

    private Boolean getIsAgree(ResultSet rs, int row) throws SQLException {
        return rs.getBoolean("is_agree");
    }

    private User makeUser(ResultSet rs, int row) throws SQLException {
        return new User((long) rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate());
    }
}
