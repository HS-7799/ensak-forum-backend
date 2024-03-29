package com.forum.ensak.models;



import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.jni.Library;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(	name = "users",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = "username"),
				@UniqueConstraint(columnNames = "email")
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonIgnore
	private String password;

	@NotBlank
	@Size(max = 20)
	private String name;

	private Boolean Is_Completed;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@OneToOne(mappedBy = "user")
	private Student student;

	@OneToOne(mappedBy = "user")
	private Company company;

	public User() {
	}

	public User(String username,String email,String name,String password,Boolean isCompleted,Set<Role> roles) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.name = name;
		this.Is_Completed = isCompleted;
		this.roles=roles;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Boolean getIs_Completed() {
		return Is_Completed;
	}

	public void setIs_Completed(Boolean is_Completed) { this.Is_Completed = is_Completed; }

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Long getStudent() {
		if(student != null)
		{
			return student.getId();
		}
		return null;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public Long getCompany() {
		if(company != null)
		{
			return company.getId();
		}
		return null;
	}

	public void setCompany(Company company) {
		this.company = company;
	}
}