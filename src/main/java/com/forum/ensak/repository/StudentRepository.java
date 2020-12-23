package com.forum.ensak.repository;

import com.forum.ensak.models.Level;
import com.forum.ensak.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {

    Student getById(Long id);

}
