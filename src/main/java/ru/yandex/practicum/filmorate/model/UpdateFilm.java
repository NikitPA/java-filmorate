package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.IncorrectBirthdayFilm;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
public class UpdateFilm {

    private final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

    @Setter(AccessLevel.NONE)
    private long id;
    @NotEmpty
    private String name;
    @Size(max = 200)
    private String description;
    @Min(0)
    private int duration;
    @Setter(AccessLevel.NONE)
    @Past()
    private LocalDate releaseDate;

    public UpdateFilm(String name, String description, int duration, LocalDate releaseDate, long id) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = setCorrectReleaseDate(releaseDate);
        this.id = id;
    }

    private LocalDate setCorrectReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(releaseDate)) {
            log.info("Не корректная дата создания фильма");
            throw new IncorrectBirthdayFilm("Дата создание фильмов 1895.12.28");
        }
        return releaseDate;
    }
}
