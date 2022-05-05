package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Set;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public ResponseEntity<Collection<Film>> getFilms() {
        return filmService.getFilms();
    }

    @Validated(Marker.onCreate.class)
    @PostMapping
    public ResponseEntity<Film> addFilm(@Valid @RequestBody Film film) {
        filmService.addFilm(film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @Validated(Marker.onUpdate.class)
    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film updateFilm) {
        Film film = filmService.updateFilm(updateFilm);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable(name = "id") Long id) {
        if (id < 0)
            throw new IncorrectId(id);
        Film filmById = filmService.getFilmById(id);
        return new ResponseEntity<>(filmById, HttpStatus.OK);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable(name = "id") Long id,
                                        @PathVariable(name = "userId") Long userId) {
        Film film = filmService.addLike(id, userId);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<String> deleteLike(@PathVariable(name = "id") Long id,
                                             @PathVariable(name = "userId") Long userId) {
        filmService.deleteLike(id, userId);
        return ResponseEntity.ok("valid");
    }

    @GetMapping("/popular")
    public ResponseEntity<Set<Film>> getTheBestPopularFilm(
            @RequestParam(name = "count", required = false, defaultValue = "10") Long count) {
        Set<Film> theBestPopularFilm = filmService.getTheBestPopularFilm(count);
        return new ResponseEntity<>(theBestPopularFilm, HttpStatus.OK);
    }
}
