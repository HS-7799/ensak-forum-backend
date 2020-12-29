package com.forum.ensak.repository;

import com.forum.ensak.models.ActivityArea;
import com.forum.ensak.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityAreaRepository extends JpaRepository<ActivityArea,Long> {
    Boolean existsByName(String name);
    ActivityArea getById(Long id);
}
