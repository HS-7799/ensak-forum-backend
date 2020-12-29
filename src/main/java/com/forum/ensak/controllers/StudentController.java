package com.forum.ensak.controllers;


import com.forum.ensak.models.Level;
import com.forum.ensak.models.Speciality;
import com.forum.ensak.models.Student;
import com.forum.ensak.models.User;
import com.forum.ensak.repository.LevelRepository;
import com.forum.ensak.repository.SpecialityRepository;
import com.forum.ensak.repository.StudentRepository;
import com.forum.ensak.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private SpecialityRepository specialityRepository;

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/students")
    public List<Student> index()
    {
        return studentRepository.findAll();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> show(@Valid @PathVariable Long id)
    {
        Student student = studentRepository.getById(id);
        if(student != null)
        {
            return new ResponseEntity<Student>(student,HttpStatus.OK);
        }
        return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/students")
    public ResponseEntity<? extends Object>  store(@Valid @RequestBody Student student)
    {
        Level level = levelRepository.getById(student.getLevel().getId());
        Speciality speciality = specialityRepository.getById(student.getSpeciality().getId());
        User user = userRepository.getById(student.getUser().getId());

        Student newStudent = new Student();
        newStudent.setUser(user);
        newStudent.setLevel(level);
        newStudent.setSpeciality(speciality);
        newStudent.setDescription(student.getDescription());
        newStudent.setCv(student.getCv());

        studentRepository.save(newStudent);

        return ResponseEntity.accepted().body(newStudent);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('ETUDIANT')")
    @PutMapping("/students/{id}")
    public ResponseEntity<Student> update(@Valid @RequestBody Student student,@PathVariable Long id)
    {

        Level level = levelRepository.getById(student.getLevel().getId());
        Speciality speciality = specialityRepository.getById(student.getSpeciality().getId());
        User user = userRepository.getById(student.getUser().getId());

        Student newStudent = studentRepository.getById(id);
        if(newStudent == null)
        {
            return new ResponseEntity<Student>(HttpStatus.NOT_FOUND);
        }
        newStudent.setLevel(level);
        newStudent.setSpeciality(speciality);
        newStudent.setCv(student.getCv());
        newStudent.setDescription(student.getDescription());
        newStudent = studentRepository.save(newStudent);

        return new ResponseEntity<Student>(newStudent,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/students/{id}")
    public void destroy(@Valid @PathVariable Long id)
    {
        Student student = studentRepository.getById(id);
        if(student != null)
        {
            this.studentRepository.deleteById(id);
        }
    }
}






