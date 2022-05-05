package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.UserNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public ResponseEntity<Collection<Film>> getFilms() {
        return new ResponseEntity<>(filmStorage.getFilms(), HttpStatus.OK);
    }

    public void addFilm(Film film) {
        filmStorage.save(film);
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id).orElseThrow(() -> new FilmNotFound(id));
    }

    public Film updateFilm(Film updateFilm) {
        Film film = filmStorage.getFilmById(updateFilm.getId()).
                orElseThrow(() -> new FilmNotFound(updateFilm.getId()));
        film.setName(updateFilm.getName());
        film.setDescription(updateFilm.getDescription());
        film.setDuration(updateFilm.getDuration());
        film.setCorrectReleaseDate(updateFilm.getReleaseDate());
        filmStorage.save(film);
        return film;
    }

    public Film addLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id).
                orElseThrow(() -> new FilmNotFound(id));
        User user = userService.getUserStorage().getById(userId).
                orElseThrow(() -> new UserNotFound(userId));
        film.getLikes().add(user.getId());
        return film;
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmStorage.getFilmById(id).
                orElseThrow(() -> new FilmNotFound(id));
        User user = userService.getUserStorage().getById(userId).
                orElseThrow(() -> new UserNotFound(userId));
        film.getLikes().remove(user.getId());
    }

    public Set<Film> getTheBestPopularFilm(Long count) {
        return filmStorage.getFilms().stream().
                sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size()).
                limit(count).collect(Collectors.toSet());
    }
}
