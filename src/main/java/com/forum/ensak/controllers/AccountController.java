package com.forum.ensak.controllers;


import com.forum.ensak.models.User;
import com.forum.ensak.repository.UserRepository;
import com.forum.ensak.request.UpdatePasswordRequest;
import com.forum.ensak.request.UpdateRequest;
import com.forum.ensak.response.JwtResponse;
import com.forum.ensak.response.MessageResponse;
import com.forum.ensak.security.jwt.JwtUtils;
import com.forum.ensak.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/auth")
public class AccountController {

    @Autowired
    private JwtUtils jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    PasswordEncoder encoder;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ENTREPRISE') or hasRole('ADMIN')")
    public User showAccount(@RequestHeader(name="Authorization") String tokenHeader) {
        final String token = tokenHeader.split(" ")[1];
        final String username =jwtUtil.getUserNameFromJwtToken(token);
        return userRepository.getByUsername(username);

    }

    @PutMapping("/profile/update/{id}")
//    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ENTREPRISE') or hasRole('ADMIN')")
    public ResponseEntity updateUser(@Valid @RequestBody UpdateRequest updateRequest, @PathVariable Long id)
    {
        String error = "";
        User currentuser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));
        if (!currentuser.getUsername().equals(updateRequest.getUsername()))
        {
            if(userRepository.existsByUsername(updateRequest.getUsername()))
            {
                error += "This username is already used,";
            }
        }

        if (!currentuser.getEmail().equals(updateRequest.getEmail()))
        {
            if (userRepository.existsByEmail(updateRequest.getEmail())) {
                error += "This email is already used,";
            }
        }
        if(error.length() > 0)
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(error));
        }
        Optional<Object> s = userRepository.findById(id)
                .map(user -> {
                    user.setName(updateRequest.getName());
                    user.setUsername(updateRequest.getUsername());
                    user.setEmail(updateRequest.getEmail());
                    return userRepository.save(user);
                });


        return ResponseEntity.ok(new MessageResponse("Informationss are updated"));

    }


    @PreAuthorize("hasRole('ETUDIANT') or hasRole('ENTREPRISE') or hasRole('ADMIN')")
    @PutMapping("/profile/updatepassword/{id}")
    public ResponseEntity updateUser(@Valid @RequestBody UpdatePasswordRequest updatepasswordrequest, @PathVariable Long id) {
        String error = "";
        User currentuser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));

        if (!updatepasswordrequest.getNewpassword().equals(updatepasswordrequest.getPasswordconfirmation())) {
            error+="your password confirmation doesn't match,";
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean isPasswordMatch = passwordEncoder.matches(updatepasswordrequest.getOldpassword(), currentuser.getPassword());
        if (!isPasswordMatch){
            error+="your old password is wrong,";
        }
        if(error.length() > 0)
        {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(error));
        }
        String passwordcrypt = encoder.encode(updatepasswordrequest.getNewpassword());
        Optional<Object> s = userRepository.findById(id)
                .map(user -> {
                    user.setPassword(passwordcrypt);
                    return userRepository.save(user);
                });


        return ResponseEntity.ok(new MessageResponse("Your password is updated successfully"));
    }

}

