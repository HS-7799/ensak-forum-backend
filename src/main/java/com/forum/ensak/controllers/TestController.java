package com.forum.ensak.controllers;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('ETUDIANT') or hasRole('ENTREPRISE') or hasRole('ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/entreprise")
	@PreAuthorize("hasRole('ENTREPRISE') or hasRole('ADMIN') ")
	public String entrepriseAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/etudiant")
	@PreAuthorize("hasRole('ETUDIATN') or hasRole('ADMIN')")
	public String etudiantAccess() {
		return "etudiant Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}

