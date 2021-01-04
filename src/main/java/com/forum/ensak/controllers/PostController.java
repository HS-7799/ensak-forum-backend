package com.forum.ensak.controllers;

import com.forum.ensak.models.*;
import com.forum.ensak.repository.CompanyRepository;
import com.forum.ensak.repository.PostRepository;
import com.forum.ensak.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping("/posts")
    public List<Post> index()
    {
        return postRepository.findAll();
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> show(@Valid @PathVariable Long id)
    {
        Post post =  postRepository.getById(id);
        if(post != null)
        {
            return new ResponseEntity<Post>(post, HttpStatus.OK);
        }
        return new ResponseEntity<Post>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ENTREPRISE')")
    @PostMapping("/posts")
    public Post store(@Valid @RequestBody Post post)
    {
        Company company = companyRepository.getById(post.getCompany().getId());
        Post newPost = new Post(post.getTitle(),post.getBody(),post.getLocation(),company);
        return postRepository.save(newPost);
    }

    @PreAuthorize("hasRole('ENTREPRISE') OR hasRole('ADMIN')")
    @PutMapping("/posts/{id}")
    public ResponseEntity update(@Valid @RequestBody Post post,@PathVariable Long id)
    {
        Post p = postRepository.findById(id)
                .map(post1 -> {
                    post1.setTitle(post.getTitle());
                    post1.setBody(post.getBody());
                    post1.setLocation(post.getLocation());
                    return postRepository.save(post1);
                })
                .orElseGet(() -> {
                    post.setId(id);
                    return postRepository.save(post);
                });
        return ResponseEntity.accepted().body(p);
    }

    @PreAuthorize("hasRole('ENTREPRISE') OR hasRole('ADMIN')")
    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable Long id)
    {
        Post post = postRepository.getById(id);
        if(post != null) {

            postRepository.deleteById(id);
        }
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @PostMapping("/posts/{postId}/{studentId}")
    public void applyForPost(@PathVariable Long postId,@PathVariable Long studentId)
    {
        Set<Student> students  = new HashSet<>();
        Post post = postRepository.getById(postId);
        
        if(post != null)
        {
            Student student = studentRepository.getById(studentId);
            if(student != null)
            {
                students.add(student);
                post.setStudents(students);
                postRepository.save(post);
            }
        }
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @DeleteMapping("/posts/{postId}/{studentId}")
    public void quit(@PathVariable Long postId,@PathVariable Long studentId)
    {
        Post post = postRepository.getById(postId);

        if(post != null)
        {
            Student student = studentRepository.getById(studentId);
            Set<Student> students  = post.getStudents();

            if(student != null && students.size() != 0)
            {
                students.remove(student);
                post.setStudents(students);
                postRepository.save(post);
            }
        }

    }

}
