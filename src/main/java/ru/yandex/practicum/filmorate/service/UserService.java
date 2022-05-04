package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IncorrectId;
import ru.yandex.practicum.filmorate.exceptions.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ResponseEntity<Collection<User>> getUsers() {
        return new ResponseEntity<>(userStorage.getUsers(), HttpStatus.OK);
    }

    public User getUserById(Long id) {
        return userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(getAnswer(id)));
    }

    public void createUser(User user) {
        userStorage.save(user);
    }

    public void updateUser(User updateUser) {
        update(updateUser);
    }

    public void addToFriends(Long id, Long friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(getAnswer(id)));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() -> new UserNotFound(getAnswer(friendId)));
        user.getSetFriends().add(friendId);
        friendUser.getSetFriends().add(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(getAnswer(id)));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() -> new UserNotFound(getAnswer(friendId)));
        user.getSetFriends().remove(friendId);
        friendUser.getSetFriends().remove(id);
    }

    public List<User> getListAllFriends(Long id) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(getAnswer(id)));
        return user.getSetFriends().stream().
                map(x -> userStorage.getById(x).orElseThrow(
                        () -> new UserNotFound(getAnswer(x)))).
                collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(getAnswer(id)));
        User otherUser = userStorage.getById(otherId)
                .orElseThrow(() -> new UserNotFound(getAnswer(otherId)));
        Set<Long> setFriends = user.getSetFriends();
        Set<Long> setOtherFriends = otherUser.getSetFriends();
        Set<Long> idUserCommon = setFriends.stream().filter(setOtherFriends::contains).collect(Collectors.toSet());
        return idUserCommon.stream().
                map(x -> userStorage.getById(x).orElseThrow(
                        () -> new UserNotFound(getAnswer(x)))).
                collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }

    private void update(User updateUser) {
        User user = userStorage.getById(updateUser.getId()).
                orElseThrow(() -> new UserNotFound(getAnswer(updateUser.getId())));
        if (user != null) {
            user.setLogin(updateUser.getLogin());
            user.setEmail(updateUser.getEmail());
            user.setCorrectName(updateUser.getName());
            user.setBirthday(updateUser.getBirthday());
        } else {
            throw new IncorrectId("Пользователя не существует");
        }
        userStorage.save(user);
    }

    private String getAnswer(Long id) {
        return "Пользователь с id" + id + " не найден.";
    }
}

