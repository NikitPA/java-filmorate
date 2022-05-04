package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectId;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Validated
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Collection<User>> getUsers() {
        return userService.getUsers();
    }

    @Validated(Marker.onCreate.class)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Validated(Marker.onUpdate.class)
    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User updateUser) {
        userService.updateUser(updateUser);
        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id) {
        if (id < 0)
            throw new IncorrectId("Неправильное переданное id: " + id);
        User userById = userService.getUserById(id);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<List<User>> addToFriends(@PathVariable(name = "id") Long id,
                                                   @PathVariable(name = "friendId") Long friendId) {
        userService.addToFriends(id, friendId);
        User userById = userService.getUserById(id);
        User userByIdFriend = userService.getUserById(friendId);
        return new ResponseEntity<>(List.of(userById, userByIdFriend), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<String> removeFromFriends(@PathVariable(name = "id") Long id,
                                                    @PathVariable(name = "friendId") Long friendId) {
        userService.removeFromFriends(id, friendId);
        return ResponseEntity.ok("valid");
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getListAllFriends(@PathVariable(name = "id") Long id) {
        List<User> listAllFriends = userService.getListAllFriends(id);
        return new ResponseEntity<>(listAllFriends, HttpStatus.OK);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getCommonFriends(@PathVariable(name = "id") Long id,
                                                       @PathVariable(name = "otherId") Long otherId) {
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        return new ResponseEntity<>(commonFriends, HttpStatus.OK);
    }
}
