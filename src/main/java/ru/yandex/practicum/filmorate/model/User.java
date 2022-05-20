package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
public class User {

    public static long count = 1;

    @Setter(AccessLevel.NONE)
    private Set<Long> friends = new HashSet<>();
    @Null(groups = Marker.onCreate.class)
    @NotNull(groups = Marker.onUpdate.class)
    private Long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @Setter(AccessLevel.NONE)
    private String name;
    @Past
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        setCorrectName(name);
        this.birthday = birthday;
    }

    public void setCorrectName(String name) {
        if (name.isBlank()) {
            this.name = login;
            return;
        }
        this.name = name;
    }
}
