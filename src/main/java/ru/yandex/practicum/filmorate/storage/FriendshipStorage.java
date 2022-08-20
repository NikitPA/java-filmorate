package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {

    void save(long user_id, long friend_id);

    void delete(long user_id, long friend_id);

    Collection<User> getFriends(long id);

    Collection<User> getCommonFriends(long user_id, long user2_id);
}
