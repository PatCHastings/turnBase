package com.javaproject.turnbase.service;

import com.javaproject.turnbase.entity.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractCharacterService<T extends GameCharacter> {
    protected abstract JpaRepository<T, Long> getRepository();

    public T save(T character) {
        return getRepository().save(character);
    }

    public T findById(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public void deleteById(Long id) {
        getRepository().deleteById(id);
    }
}

