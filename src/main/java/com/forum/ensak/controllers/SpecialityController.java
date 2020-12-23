package com.forum.ensak.controllers;

import com.forum.ensak.models.Speciality;
import com.forum.ensak.repository.SpecialityRepository;
import com.forum.ensak.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SpecialityController {

    private SpecialityRepository specialityRepository;

    public SpecialityController(SpecialityRepository specialityRepository)
    {
        this.specialityRepository = specialityRepository;
    }

    @GetMapping("/specialities")
    public List<Speciality> index()
    {
        return specialityRepository.findAll();
    }

    @GetMapping("/specialities/{id}")
    public ResponseEntity<Speciality> show(@Valid @PathVariable Long id)
    {
        Speciality speciality = specialityRepository.getById(id);
        if(speciality != null)
        {
            return new ResponseEntity<Speciality>(speciality, HttpStatus.OK);
        }
        return new ResponseEntity<Speciality>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/specialities")
    public ResponseEntity store(@Valid @RequestBody Speciality newSpeciality)
    {
        if(specialityRepository.existsByName(newSpeciality.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already in use!"));
        }

        Speciality speciality = specialityRepository.save(newSpeciality);
        return ResponseEntity.accepted().body(speciality);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/specialities/{id}")
    public ResponseEntity update(@Valid @RequestBody Speciality newSpeciality,@PathVariable Long id)
    {
        if(specialityRepository.existsByName(newSpeciality.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already in use!"));
        }

        Speciality s = specialityRepository.findById(id)
                .map(speciality -> {
                    speciality.setName(newSpeciality.getName());
                    speciality.setLabel(newSpeciality.getLabel());
                    speciality.setStudents(newSpeciality.getStudents());
                    return specialityRepository.save(speciality);
                })
                .orElseGet(() -> {
                    newSpeciality.setId(id);
                    return specialityRepository.save(newSpeciality);
                });
        return ResponseEntity.accepted().body(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/specialities/{id}")
    public ResponseEntity destroy(@Valid @PathVariable Long id)
    {
        Speciality speciality = specialityRepository.getById(id);
        if(speciality != null)
        {
            if(speciality.getStudents().size() != 0)
                return ResponseEntity.badRequest().body(new MessageResponse("Some students have this speciality"));
            this.specialityRepository.deleteById(id);
        }

        return ResponseEntity.ok(null);
    }

}
