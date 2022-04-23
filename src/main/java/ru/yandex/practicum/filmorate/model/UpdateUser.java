package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UpdateUser {

    private long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    @Setter(AccessLevel.NONE)
    private String name;
    @Past
    private LocalDate birthday;

    public UpdateUser(String email, String login, String name, LocalDate birthday, long id) {
        this.email = email;
        this.login = login;
        this.name = setCorrectName(name);
        this.birthday = birthday;
        this.id = id;
    }

    public String setCorrectName(String name) {
        if (name.isBlank())
            return login;
        return name;
    }
}
