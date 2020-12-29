package com.forum.ensak.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "activityareas")
public class ActivityArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is a required field")
    @Size(min = 5,message = "Name should have at least 5 characters")
    private String name;

    private String label;

    @JsonIgnore
    @OneToMany(mappedBy = "activityarea")
    private List<Company> companies;


    public ActivityArea(String name,String label) {
        this.name = name;
        this.label = label;
    }

    private ActivityArea() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
