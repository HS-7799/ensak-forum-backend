package com.forum.ensak.controllers;

import com.forum.ensak.models.*;
import com.forum.ensak.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ActivityAreaRepository activityareaRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTREPRISE') or hasRole('ETUDIANT')")
    @GetMapping("/companies")
    public List<Company> index()
    {
        return companyRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTREPRISE') or hasRole('ETUDIANT')")
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> show(@Valid @PathVariable Long id)
    {
        Company company = companyRepository.getById(id);
        if(company != null)
        {
            return new ResponseEntity<Company>(company, HttpStatus.OK);
        }
        return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTREPRISE')")
    @PostMapping("/companies")
    public ResponseEntity<? extends Object> store(@Valid @RequestBody Company company)
    {

        ActivityArea activityarea = activityareaRepository.getById(company.getActivityarea().getId());
        User user = userRepository.getById(company.getUser().getId());

        Company newCompany = new Company();
        newCompany.setUser(user);
        newCompany.setActivityarea(activityarea);
        newCompany.setAddress(company.getAddress());
        newCompany.setDescription(company.getDescription());
        newCompany.setLogo(company.getLogo());

        companyRepository.save(newCompany);

        return ResponseEntity.accepted().body(newCompany);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ENTREPRISE')")
    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> update(@Valid @RequestBody Company company,@PathVariable Long id)
    {
        ActivityArea activityarea = activityareaRepository.getById(company.getActivityarea().getId());
        User user = userRepository.getById(company.getUser().getId());

        Company newCompany = companyRepository.getById(id);
        if(newCompany == null)
        {
            return new ResponseEntity<Company>(HttpStatus.NOT_FOUND);
        }
        newCompany.setUser(user);
        newCompany.setActivityarea(activityarea);
        newCompany.setAddress(company.getAddress());
        newCompany.setLogo(company.getLogo());
        newCompany.setDescription(company.getDescription());
        newCompany = companyRepository.save(newCompany);

        return new ResponseEntity<Company>(newCompany,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/companies/{id}")
    public void destroy(@Valid @PathVariable Long id)
    {
        Company company = companyRepository.getById(id);

        if(company != null) {
            this.companyRepository.deleteById(id);
        }
    }


    @GetMapping("/companies/{id}/posts")
    public List<Post> companyPosts(@PathVariable Long id)
    {

        Company company = companyRepository.getById(id);
        if(company != null)
        {
            return company.getPosts();
        }

        return null;
    }
}







