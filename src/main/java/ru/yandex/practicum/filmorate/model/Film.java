package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.IncorrectBirthdayFilm;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
@NoArgsConstructor
public class Film {
    public static final LocalDate BIRTHDAY_MOVIE = LocalDate.of(1895, 12, 28);

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
    private MPA mpa;
    private Collection<Genre> genres;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Collection<User> usersLike = new HashSet<>();

    public Film(Long id, String name, String description,  LocalDate releaseDate , int duration,
                Collection<User> usersLike, MPA mpa, Collection<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        setCorrectReleaseDate(releaseDate);
        this.duration = duration;
        this.usersLike = usersLike;
        this.mpa = mpa;
        this.genres = genres;
    }

    public void setCorrectReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(BIRTHDAY_MOVIE)) {
            throw new IncorrectBirthdayFilm(releaseDate.toString());
        }
        this.releaseDate = releaseDate;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("name", name);
        values.put("description", description);
        values.put("release_date", releaseDate);
        values.put("duration", duration);
        values.put("mpa", mpa.toString());
        return values;
    }
}
