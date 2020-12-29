package com.forum.ensak.repository;

import com.forum.ensak.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Long> {
    Company getById(Long id);
}
