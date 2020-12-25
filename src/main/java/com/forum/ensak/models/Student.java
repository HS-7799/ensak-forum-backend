package com.forum.ensak.models;


import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull(message = "Level is required")
    @ManyToOne
    @JoinColumn(name="level_id",nullable = false)
    private Level level;

    @NotNull(message = "Speciality is required")
    @ManyToOne
    @JoinColumn(name="speciality_id",nullable = false)
    private Speciality speciality;

    private String description;
    private String cv;

    public Student(Long id, User user, Level level, Speciality speciality, String description, String cv) {
        this.id = id;
        this.user = user;
        this.level = level;
        this.speciality = speciality;
        this.description = description;
        this.cv = cv;
    }

    public Student() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }
}
