package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Slf4j
public class User {

    private static long count = 1;

    @Setter(AccessLevel.NONE)
    @JsonIgnore
    private final long id;
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
        this.name = setCorrectName(name);
        this.birthday = birthday;
        this.id = count++;
    }

    public String setCorrectName(String name) {
        if (name.isBlank()) {
            log.info("У пользователя пустое имя ,использован логин.");
            return login;
        }
        return name;
    }
}
