package ru.yandex.practicum.filmorate.exceptions;

public class FilmNotFound extends RuntimeException {

    public FilmNotFound(String message) {
        super(message);
    }
}
