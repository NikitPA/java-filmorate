package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFound;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class MpaService {

    public MPA findMpaById(Integer id) {
        for (MPA obj : MPA.values()) {
            if (Double.compare(obj.getId(), id) == 0) {
                return obj;
            }
        }
        throw new MpaNotFound(String.format("MPA c id № %d не найден", id));
    }

    public Collection<MPA> returnAllMpa() {
        Collection<MPA> mpas = new HashSet<>();
        for (MPA obj : MPA.values()) {
            mpas.add(obj);
        }
        return mpas.stream().sorted(Comparator.comparingInt(MPA::getId)).collect(Collectors.toList());

    }
}
