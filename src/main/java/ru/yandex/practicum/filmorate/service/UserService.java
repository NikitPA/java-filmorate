package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public ResponseEntity<Map<Long, User>> getUsers() {
        return new ResponseEntity<>(userStorage.getUsers(), HttpStatus.OK);
    }

    public User getUserById(Long id) {
        User user = userStorage.getUsersById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id " + id + " не найден."));
                return user;
    }

    public void createUser(User user) {
        userStorage.createUser(user);
    }

    public void updateUser(User updateUser) {
        userStorage.updateUser(updateUser);
    }

    public void addToFriends(Long id, Long friendId) {
        User user = userStorage.getUsersById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + id + " не найден."));
        User friendUser = userStorage.getUsersById(friendId)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + friendId + " не найден."));
        user.getSetFriends().add(friendId);
        friendUser.getSetFriends().add(id);
    }

    public void removeFromFriends(Long id, Long friendId) {
        User user = userStorage.getUsersById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + id + " не найден."));
        User friendUser = userStorage.getUsersById(friendId)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + friendId + " не найден."));
        user.getSetFriends().remove(friendId);
        friendUser.getSetFriends().remove(id);
    }

    public List<User> getListAllFriends(Long id) {
        User user = userStorage.getUsersById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + id + " не найден."));
        return user.getSetFriends().stream().
                map(x -> userStorage.getUsersById(x).orElseThrow(
                        () -> new UserNotFound("Пользователь с id" + x + " не найден."))).
                collect(Collectors.toList());
    }

    public List<User>  getCommonFriends(Long id , Long otherId){
        User user = userStorage.getUsersById(id)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + id + " не найден."));
        User otherUser = userStorage.getUsersById(otherId)
                .orElseThrow(() -> new UserNotFound("Пользователь с id" + otherId + " не найден."));
        Set<Long> setFriends = user.getSetFriends();
        Set<Long> setOtherFriends = otherUser.getSetFriends();
        Set<Long> idUserCommon = setFriends.stream().filter(setOtherFriends::contains).collect(Collectors.toSet());
        return idUserCommon.stream().
                map(x -> userStorage.getUsersById(x).orElseThrow(
                        () -> new UserNotFound("Пользователь с id" + x + " не найден."))).
                collect(Collectors.toList());
    }

    public UserStorage getUserStorage() {
        return userStorage;
    }
}

