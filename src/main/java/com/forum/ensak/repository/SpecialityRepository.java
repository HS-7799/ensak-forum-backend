package com.forum.ensak.repository;

import com.forum.ensak.models.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpecialityRepository extends JpaRepository<Speciality,Long> {

    Boolean existsByName(String name);
    Speciality getById(Long id);
}
