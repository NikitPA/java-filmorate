package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap();

    @Override
    public void createUser(User user) {
        user.setId(User.count++);
        users.put(user.getId(), user);
        log.info("Объект User добавлен");
    }

    @Override
    public void updateUser(User updateUser) {
        update(updateUser);
        log.info("Объект User обновлён");
    }

    private void update(User updateUser) {
        User user = users.get(updateUser.getId());
        if (user != null) {
            user.setLogin(updateUser.getLogin());
            user.setEmail(updateUser.getEmail());
            user.setCorrectName(updateUser.getName());
            user.setBirthday(updateUser.getBirthday());
        } else {
            throw new NotFoundId("Пользователя не существует");
        }
        users.put(user.getId(), user);
    }

    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public Optional<User> getUsersById(Long id) {
        return Optional.of(users.get(id));
    }
}
