package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IncorrectId extends IllegalArgumentException {

    public IncorrectId(Long id) {
        super(String.format("Неправильное переданное id: %d", id));
    }
}
