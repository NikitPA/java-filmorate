package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
public class FilmDbStorage implements FilmStorage {

    private final String saveGenreQuery = "INSERT INTO films_genre(film_id, genre_id) VALUES (?, ?)";
    private final String findGenreQuery = "SELECT * FROM genres WHERE genre_id = ?";
    private final String findGenreForFilmQuery = "SELECT * FROM films_genre WHERE film_id = ? AND genre_id = ?";
    private final String deleteFilm = "DELETE FROM films WHERE film_id = ?";
    private final String updateQuery =
            "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?,  mpa = ? WHERE film_id =?";
    private final String deleteFilmGenres = "DELETE FROM films_genre WHERE film_id = ?";
    private final String getGenreQuery =
            "SELECT g.name from genres g JOIN films_genre f on g.genre_id = f.genre_id WHERE film_id = ?";
    private final String getFilmsQuery ="SELECT * from films";
    private final String findByIdQuery = "SELECT * FROM films WHERE film_id = ?";
    private final String getPopularQuery = "SELECT " +
            "f.film_id, f.name, f.description, f.release_date,f.duration, f.mpa films " +
            "FROM films AS f LEFT JOIN likes AS l on f.film_id = l.film_id " +
            "GROUP BY f.film_id " +
            "ORDER BY COUNT(DISTINCT l.user_id) DESC LIMIT ?";

    private final JdbcTemplate jdbcTemplate;
    private final UserDbStorage userDbStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate , UserDbStorage userDbStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDbStorage = userDbStorage;
    }

    @Override
    public long save(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("films").
                usingGeneratedKeyColumns("film_id");
        int film_id = jdbcInsert.executeAndReturnKey(film.toMap()).intValue();
        if (film.getGenres() != null) {
            saveGenre(film_id, film.getGenres());
        }
        return film_id;
    }

    private void saveGenre(long film_id, Collection<Genre> genres) {
        for (Genre genre : genres) {
            if (findGenre(genre.getId())) {
                if (!findGenreForFilm(genre.getId(), film_id)) {
                    jdbcTemplate.update(saveGenreQuery,
                            film_id,
                            genre.getId());
                }
            }
        }
    }

    private boolean findGenre(long id) {
        if (jdbcTemplate.queryForRowSet(findGenreQuery, id).next())
            return true;
        return false;
    }

    private boolean findGenreForFilm(long genre_id, long film_id) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(findGenreForFilmQuery, film_id, genre_id);
        if (genreRows.next())
            return true;
        return false;
    }

    @Override
    public Collection<Film> getFilms() {
        return jdbcTemplate.query(getFilmsQuery , this::makeFilm);
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return jdbcTemplate.query(
                findByIdQuery, this::makeFilm, id
        ).stream().findAny();
    }

    @Override
    public void update(Film film) {
        jdbcTemplate.update(updateQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().toString(),
                film.getId());
        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                deleteGenre(film.getId());
                saveGenre(film.getId(), film.getGenres());
            } else {
                deleteGenre(film.getId());//////////////////////////////////
            }
        }
    }

    private void deleteGenre(Long film_id) {
        jdbcTemplate.update(deleteFilmGenres,
                film_id);
    }

    @Override
    public void delete(Long film_id) {
        deleteGenre(film_id);
        jdbcTemplate.update(deleteFilm,
                film_id);
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Integer film_id = rs.getInt("film_id");
        return new Film((long)rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getShort("duration"),
                userDbStorage.findUsersLikeToFilm(film_id),
                MPA.valueOf(rs.getString("mpa")),
                getGenres(film_id)
        );
    }

    private Collection<Genre> getGenres(int film_id) {
        Collection<Genre> genres = jdbcTemplate.query(getGenreQuery, this::makeGenre, film_id);
        if (!genres.isEmpty()) {
            return genres;
        } else {
            return null;
        }
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("name");

        Genre genre = Genre.valueOf(name);
        return genre;
    }

    public Collection<Film> getPopular(long limit) {
        return jdbcTemplate.query(getPopularQuery, (this::makeFilm), limit);
    }
}
