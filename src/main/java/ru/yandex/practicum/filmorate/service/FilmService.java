package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.UserNotFound;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FilmService {


    private final FilmDbStorage filmStorage;
    private final UserService userService;
    private LikeStorage likeStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage,
                       UserService userService , LikeStorage likeStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likeStorage = likeStorage;
    }

    public ResponseEntity<Collection<Film>> getFilms() {
        return new ResponseEntity<>(filmStorage.getFilms(), HttpStatus.OK);
    }

    public Film addFilm(Film film) {
        long idSaveFilm = filmStorage.save(film);
        return getFilmById(idSaveFilm);
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
        film.setUsersLike(updateFilm.getUsersLike());
        film.setMpa(updateFilm.getMpa());
        film.setGenres(updateFilm.getGenres());
        filmStorage.update(film);
        Film filmSearch = getFilmById(film.getId());
        if (film.getGenres()!= null && film.getGenres().isEmpty()){
            if (filmSearch.getGenres() == null){
                filmSearch.setGenres(new HashSet<>());
            }
        }
        return filmSearch;
    }

    public void deleteFilm(Long film_id) {
        if (getFilmById(film_id) != null) {
            filmStorage.delete(film_id);
        }
    }

    public void addLike(Long film_id, Long userId) {
        if (getFilmById(film_id) != null && userService.getUserById(userId) != null) {
            likeStorage.save(film_id, userId);
        }
    }

    public void deleteLike(Long id, Long userId) {
        if (getFilmById(id) != null && userService.getUserById(userId) != null) {
            likeStorage.delete(id, userId);
        }
    }

    public Collection<Film> getTheBestPopularFilm(Long count) {
        return filmStorage.getPopular(count);
    }
}
