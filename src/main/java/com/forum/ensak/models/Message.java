package com.forum.ensak.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Body is required")
    @Size(max = 1500,message = "Body must be less than 1500 character")
    private String body;

    @NotNull(message = "Company is required")
    @ManyToOne
    @JoinColumn(name="company_id",nullable = false)
    private Company company;


    @NotNull(message = "Student is required")
    @ManyToOne
    @JoinColumn(name="student_id",nullable = false)
    private Student student;

    @NotNull(message = "Post is required")
    @ManyToOne
    @JoinColumn(name="post_id",nullable = false)
    private Post post;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public Message(@NotBlank(message = "Body is required") @Size(max = 1500, message = "Body must be less than 1500 character") String body, @NotNull(message = "Company is required") Company company, @NotNull(message = "Student is required") Student student, @NotNull(message = "Post is required") Post post) {
        this.body = body;
        this.company = company;
        this.student = student;
        this.post = post;
    }

    public Message() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


}
