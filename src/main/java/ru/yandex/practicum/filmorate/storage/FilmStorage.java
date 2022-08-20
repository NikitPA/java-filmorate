package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    long save(Film film);

    Collection<Film> getFilms();

    Optional<Film> getFilmById(Long id);

    void update(Film film);

    void delete(Long film_id);
}
