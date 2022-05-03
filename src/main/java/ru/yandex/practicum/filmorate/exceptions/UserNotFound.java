package ru.yandex.practicum.filmorate.exceptions;

public class UserNotFound extends RuntimeException{

    public UserNotFound(String message) {
        super(message);
    }
}
