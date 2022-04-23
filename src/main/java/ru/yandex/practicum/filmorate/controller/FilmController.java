package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UpdateFilm;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public ResponseEntity<Map<Long, Film>> getFilms() {
        return new ResponseEntity<Map<Long, Film>>(films, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> addFilm(@Valid @RequestBody Film film) {
        films.put(film.getId(), film);
        log.info("Объект Film добавлен");
        return new ResponseEntity<Long>(film.getId(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Long> updateFilm(@Valid @RequestBody UpdateFilm updateFilm) {
        update(updateFilm);
        log.info("Объект Film обновлён");
        return new ResponseEntity<Long>(updateFilm.getId(), HttpStatus.OK);
    }

    private void update(UpdateFilm updateFilm) {
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
