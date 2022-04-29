package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<Map<Long, Film>> getFilms() {
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    @Validated(Marker.onCreate.class)
    @PostMapping
    public ResponseEntity<Long> addFilm(@Valid @RequestBody Film film) {
        film.setId(Film.count++);
        films.put(film.getId(), film);
        log.info("Объект Film добавлен");
        return new ResponseEntity<>(film.getId() , HttpStatus.OK);
    }

    @Validated(Marker.onUpdate.class)
    @PutMapping
    public ResponseEntity<Long> updateFilm(@Valid @RequestBody Film updateFilm) {
        update(updateFilm);
        log.info("Объект Film обновлён");
        return new ResponseEntity<>(updateFilm.getId(), HttpStatus.OK);
    }

    private void update(Film updateFilm) {
        Film film = films.get(updateFilm.getId());
        if (film != null) {
            film.setName(updateFilm.getName());
            film.setDescription(updateFilm.getDescription());
            film.setDuration(updateFilm.getDuration());
            film.setCorrectReleaseDate(updateFilm.getReleaseDate());
        } else {
            throw new NotFoundId("Пользователя не существует");
        }
        films.put(film.getId(), film);
    }
}
