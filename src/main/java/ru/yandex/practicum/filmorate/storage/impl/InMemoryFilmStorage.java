package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void addFilm(Film film) {
        film.setId(Film.count++);
        films.put(film.getId(), film);
        log.info("Объект Film добавлен");
    }

    @Override
    public Film updateFilm(Film updateFilm) {
        Film film = update(updateFilm);
        log.info("Объект Film обновлён");
        return film;
    }

    private Film update(Film updateFilm) {
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
        return film;
    }

    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return Optional.of(films.get(id));
    }
}
