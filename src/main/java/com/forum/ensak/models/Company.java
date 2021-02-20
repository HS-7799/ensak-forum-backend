package com.forum.ensak.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "Companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "user_id")
    private User user;


    @NotNull(message = "Activity area is required")
    @ManyToOne
    @JoinColumn(name="ActivityArea_id",nullable = false)
    private ActivityArea activityarea;

    @JsonIgnore
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Post> posts;

    @JsonIgnore
    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL)
    private List<Message> messages;

    @Size(max = 200,message = "Description must be less than 200 character")
    private String address;
    @Size(max = 1500,message = "Description must be less than 1500 character")
    private String description;
    private String logo;

    public Company(Long id, User user, ActivityArea activityarea, String address, String description, String logo) {
        this.id = id;
        this.user = user;
        this.activityarea = activityarea;
        this.address = address;
        this.description = description;
        this.logo = logo;
    }

    public Company() {}

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

    public ActivityArea getActivityarea() {
        return activityarea;
    }

    public void setActivityarea(ActivityArea activityarea) {
        this.activityarea = activityarea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
