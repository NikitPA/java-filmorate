package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
                .orElseThrow(() -> new UserNotFound(id));
    }

    public void createUser(User user) {
        userStorage.save(user);
    }

    public User updateUser(User updateUser) {
        User user = userStorage.getById(updateUser.getId()).
                orElseThrow(() -> new UserNotFound(updateUser.getId()));
        user.setLogin(updateUser.getLogin());
        user.setEmail(updateUser.getEmail());
        user.setCorrectName(updateUser.getName());
        user.setBirthday(updateUser.getBirthday());
        userStorage.save(user);
        return user;
    }

    public void addToFriends(Long id, Long friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(id));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() -> new UserNotFound(friendId));
        user.getFriends().add(friendId);
        friendUser.getFriends().add(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(id));
        User friendUser = userStorage.getById(friendId)
                .orElseThrow(() -> new UserNotFound(friendId));
        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(id);
    }

    public List<User> getListAllFriends(Long id) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(id));
        return user.getFriends().stream().
                map(x -> userStorage.getById(x).orElseThrow(
                        () -> new UserNotFound(x))).
                collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        User user = userStorage.getById(id)
                .orElseThrow(() -> new UserNotFound(id));
        User otherUser = userStorage.getById(otherId)
                .orElseThrow(() -> new UserNotFound(otherId));
        Set<Long> setFriends = user.getFriends();
        Set<Long> setOtherFriends = otherUser.getFriends();
        Set<Long> idUserCommon = setFriends.stream().filter(setOtherFriends::contains).collect(Collectors.toSet());
        return idUserCommon.stream().
                map(x -> userStorage.getById(x).orElseThrow(
                        () -> new UserNotFound(x))).
                collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}

