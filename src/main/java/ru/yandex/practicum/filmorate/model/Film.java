package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.IncorrectBirthdayFilm;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
public class Film {

    public static long count = 1;
    public static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

    @Setter(AccessLevel.NONE)
    private Set<Long> setLikes = new HashSet<>();
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    private Long id;
    @NotEmpty
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @Min(0)
    private int duration;
    @Setter(AccessLevel.NONE)
    @Past()
    private LocalDate releaseDate;

    public Film(String name, String description, int duration, LocalDate releaseDate) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        setCorrectReleaseDate(releaseDate);
    }

    public void setCorrectReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(BIRTHDAY_MOVIE)) {
            log.info("Не корректная дата создания фильма");
            throw new IncorrectBirthdayFilm("Дата создание фильмов 1895.12.28");
        }
        this.releaseDate = releaseDate;
    }
}
