package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Validated
@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap();

    @GetMapping
    public ResponseEntity<Map<Long, User>> getUsers() {
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Validated(Marker.onCreate.class)
    @PostMapping
    public ResponseEntity<Long> createUser(@Valid @RequestBody User user) {
        user.setId(User.count++);
        users.put(user.getId(), user);
        log.info("Объект User добавлен");
        return new ResponseEntity<>(user.getId(), HttpStatus.OK);
    }

    @Validated(Marker.onUpdate.class)
    @PutMapping
    public ResponseEntity<Long> updateUser(@Valid @RequestBody User updateUser) {
        update(updateUser);
        log.info("Объект User обновлён");
        return new ResponseEntity<>(updateUser.getId(), HttpStatus.OK);
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
}
