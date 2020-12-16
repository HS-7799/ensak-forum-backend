package com.forum.ensak.request;


import java.util.Set;

import javax.validation.constraints.*;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 20)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    private Boolean Is_Completed;
    public SignupRequest(){}

    public SignupRequest(@NotBlank @Size(min = 3, max = 20) String username, @NotBlank @Size(min = 6, max = 20) String name, @NotBlank @Size(max = 50) @Email String email, Set<String> role, @NotBlank @Size(min = 6, max = 40) String password, Boolean is_Completed) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
        this.Is_Completed = is_Completed;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Set<String> getRole() {
        return this.role;
    }

    public void setRole(Set<String> role) {
        this.role = role;
    }

    public Boolean getIs_Completed() {
        return Is_Completed;
    }

    public void setIs_Completed(Boolean is_Completed) {
        Is_Completed = is_Completed;
    }
}