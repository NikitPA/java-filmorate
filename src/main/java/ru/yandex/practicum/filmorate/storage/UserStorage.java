package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    long save(User user);

    Collection<User> getUsers();

    Optional<User> getUserById(Long id);

    void update(User user);

    void delete(Integer user_id);
}
