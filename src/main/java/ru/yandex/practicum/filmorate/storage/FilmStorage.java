package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;
import java.util.Optional;

public interface FilmStorage {

    void addFilm(Film film);

    Film updateFilm(Film updateFilm);

    Map<Long, Film> getFilms();

    public Optional<Film> getFilmById(Long id);
}
