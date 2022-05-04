package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    void save(User user);

    Collection<User> getUsers();

    Optional<User> getById(Long id);
}
