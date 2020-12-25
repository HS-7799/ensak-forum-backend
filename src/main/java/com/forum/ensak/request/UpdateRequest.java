package com.forum.ensak.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UpdateRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Name is required")
    @Size(min = 6, max = 20)
    private String name;

    @NotBlank(message = "Email is required")
    @Size(max = 50)
    @Email(message = "Email is invalid")
    private String email;

    public UpdateRequest(){}

    public UpdateRequest(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(min = 6, max = 20) String name, @NotBlank @Size(max = 50) @Email String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}