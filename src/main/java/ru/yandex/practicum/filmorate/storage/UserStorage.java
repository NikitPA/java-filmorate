package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;
import java.util.Optional;

public interface UserStorage {

    public void createUser(User user);

    public void updateUser(User updateUser);

    public Map<Long, User> getUsers();

    public Optional<User> getUsersById(Long id);
}
