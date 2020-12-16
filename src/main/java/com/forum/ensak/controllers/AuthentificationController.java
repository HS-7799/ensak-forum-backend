package com.forum.ensak.controllers;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;


import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.forum.ensak.models.ERole;
import com.forum.ensak.models.Role;
import com.forum.ensak.models.User;
import com.forum.ensak.request.LoginRequest;
import com.forum.ensak.request.SignupRequest;
import com.forum.ensak.response.JwtResponse;
import com.forum.ensak.response.MessageResponse;
import com.forum.ensak.repository.RoleRepository;
import com.forum.ensak.repository.UserRepository;
import com.forum.ensak.security.jwt.JwtUtils;
import com.forum.ensak.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthentificationController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(jwt,
				 userDetails.getId(),
				 userDetails.getUsername(),
				 userDetails.getEmail(),
				 userDetails.getName(),
				 roles));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
         signUpRequest.setIs_Completed(false);
		String errors = "";
         if(!EmailValidator.getInstance().isValid(signUpRequest.getEmail()))
		 {
		 	errors += "Email is not valid!,";

		 }

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			errors += "Username is already in use!,";
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			errors += "Email is already in use!,";
		}

		if(signUpRequest.getPassword().length() < 6 || signUpRequest.getPassword().length() > 40)
		{
			errors += "password must be less than 40 and greater than 6,";
		}

		if(signUpRequest.getUsername().trim().length() < 3  || signUpRequest.getUsername().trim().length() > 20)
		{
			errors += "username must be less than 40 and greater than 3,";
		}

		if(signUpRequest.getPassword().trim().length() == 0 || signUpRequest.getName().trim().length() == 0 || signUpRequest.getUsername().trim().length() == 0 || signUpRequest.getEmail().trim().length() == 0)
		{
			errors += "All field are required,";
		}
		if(errors.length() > 0)
		{
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse(errors));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), 
							 signUpRequest.getEmail(),
							 signUpRequest.getName(),
							 encoder.encode(signUpRequest.getPassword()),false,null);

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_ETUDIANT)
					.orElseThrow(() -> new RuntimeException("Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
				Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
					roles.add(adminRole);
					break;
				case "entreprise":
					Role modRole = roleRepository.findByName(ERole.ROLE_ENTREPRISE)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
					roles.add(modRole);
					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_ETUDIANT)
							.orElseThrow(() -> new RuntimeException("Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}

