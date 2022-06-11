package ru.yandex.practicum.filmorate.storage;

import java.util.Map;
import java.util.Set;

public interface LikeStorage {

    void save(Long film_id, Long user_id);

    void delete(Long film_id, Long user_id);
}
