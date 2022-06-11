package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFound;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage , FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public ResponseEntity<Collection<User>> getUsers() {
        return new ResponseEntity<>(userStorage.getUsers(), HttpStatus.OK);
    }

    public User getUserById(Long id) {
        return userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFound(id));
    }

    public User createUser(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        long idSaveUser = userStorage.save(user);
        return getUserById(idSaveUser);
    }

    public User updateUser(User updateUser) {
        User user = userStorage.getUserById(updateUser.getId()).
                orElseThrow(() -> new UserNotFound(updateUser.getId()));
        user.setLogin(updateUser.getLogin());
        user.setEmail(updateUser.getEmail());
        user.setCorrectName(updateUser.getName());
        user.setBirthday(updateUser.getBirthday());
        userStorage.update(user);
        return user;
    }

    public void addToFriends(Long id, Long friendId) {
        User user = userStorage.getUserById(id)
                .orElseThrow(() -> new UserNotFound(id));
        User friendUser = userStorage.getUserById(friendId)
                .orElseThrow(() -> new UserNotFound(friendId));
        friendshipStorage.save(user.getId() , friendUser.getId());
    }

    public void removeFromFriends(Long id, Long friendId) {
        if (getUserById(id) != null && getUserById(friendId) != null) {
            friendshipStorage.delete(id, friendId);
        }
    }

    public Collection<User> getListAllFriends(Long id) {
        return friendshipStorage.getFriends(id);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        return friendshipStorage.getCommonFriends(id, otherId);
    }
    public UserStorage getUserStorage() {
        return userStorage;
    }
}

