package com.forum.ensak.models;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "specialities")
public class Speciality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is a required field")
    @Size(min = 5,message = "Name should have at least 5 characters")
    private String name;

//    @NotBlank(message = "Label is a required field")
//    @Size(min = 5,message = "Label should have at least 5 characters")
    private String label;

    public Speciality(String name,String label) {
        this.name = name;
        this.label = label;
    }

    private Speciality() { }

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
}
