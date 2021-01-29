package com.forum.ensak.controllers;


import com.forum.ensak.models.*;
import com.forum.ensak.repository.*;
import com.forum.ensak.security.jwt.JwtUtils;
import com.forum.ensak.services.DBFileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private DBFileRepository dbfileRepository;

    private FileController filecontroller;

    @Autowired
    private DBFileStorageService dbFileStorageService;


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

        /**MultipartFile cv = student.getCv();
         UploadFileResponse response =filecontroller.uploadFile(cv);

         DBFile dbFile = dbFileStorageService.getFile(response.getId());**/


        Student newStudent = new Student();
        newStudent.setUser(user);
        newStudent.setLevel(level);
        newStudent.setSpeciality(speciality);
        newStudent.setDescription(student.getDescription());
        newStudent.setFileDownloadUri(student.getFileDownloadUri());
        //newStudent.setCv(cv);

        studentRepository.save(newStudent);

        return ResponseEntity.ok(newStudent.getId());
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
        //newStudent.setCv(student.getCv());
        newStudent.setDescription(student.getDescription());
        newStudent = studentRepository.save(newStudent);

        return new ResponseEntity<Student>(newStudent,HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/students/{id}")
    public void destroy(@Valid @PathVariable Long id)
    {
        Student student = studentRepository.getById(id);

        DBFile dbfile=dbfileRepository.getByFileDownloadUri(student.getFileDownloadUri());

        if(student != null)
        {
            this.studentRepository.deleteById(id);
            dbfileRepository.deleteById(dbfile.getId());
        }

    }


    @PreAuthorize("hasRole('ETUDIANT')")
    @GetMapping("/students/{id}/posts")
    public Set<Post> studentJobs(@PathVariable Long id, @RequestHeader(name="Authorization") String tokenHeader)
    {
        final String token = tokenHeader.split(" ")[1];
        final String username = jwtUtils.getUserNameFromJwtToken(token);

        Student student = studentRepository.getById(id);
        if(student != null && userRepository.getByUsername(username).getStudent() == id)
        {
            Set<Post> jobs =  student.getPosts();
            for(Post job : jobs)
            {
                job.setStudents(null);
            }
            return jobs;
        }
        return null;
    }
}






