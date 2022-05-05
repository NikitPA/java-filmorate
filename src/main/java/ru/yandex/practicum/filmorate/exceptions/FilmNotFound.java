package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FilmNotFound extends RuntimeException {

    public FilmNotFound(Long id) {
        super(String.format("Фильм с id %d не найден." , id));

    }
}
