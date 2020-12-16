package com.forum.ensak.repository;

import com.forum.ensak.models.Level;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LevelRepository extends JpaRepository<Level,Long> {

    Boolean existsByName(String name);
    Level getById(Long id);
}
