package com.forum.ensak.controllers;


import com.forum.ensak.models.User;
import com.forum.ensak.repository.UserRepository;
import com.forum.ensak.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/auth")
public class AccountController {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ENTREPRISE') or hasRole('ADMIN')")
    public User showAccount(@RequestHeader(name="Authorization") String tokenHeader) {
        final String token = tokenHeader.split(" ")[1];
        final String username =jwtUtil.getUserNameFromJwtToken(token);
        return userRepository.getByUsername(username);

    }


}

