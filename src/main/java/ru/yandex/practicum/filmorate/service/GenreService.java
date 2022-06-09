package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFound;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class GenreService {

    public Genre findGenreById(Integer id) {
        for (Genre obj : Genre.values()) {
            if (Double.compare(obj.getId(), id) == 0) {
                return obj;
            }
        }
        throw new GenreNotFound(String.format("Genre c id № %d не найден", id));
    }

    public Collection<Genre> returnAllGenres() {
        Collection<Genre> genres = new HashSet<>();
        for (Genre obj : Genre.values()) {
            genres.add(obj);
        }
        return genres.stream().sorted(Comparator.comparingInt(Genre::getId)).collect(Collectors.toList());

    }
}
