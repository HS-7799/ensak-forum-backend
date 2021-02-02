package com.forum.ensak.controllers;


import com.forum.ensak.models.*;
import com.forum.ensak.repository.*;
import com.forum.ensak.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserRepository userRepository;


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/messages")
    public List<Message> index()
    {
        return messageRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/messages/{id}")
    public Message show(@PathVariable Long id)
    {
        return messageRepository.getById(id);
    }

    @PreAuthorize("hasRole('ENTREPRISE')")
    @PostMapping("/messages")
    public ResponseEntity<Message> store(@Valid @RequestBody Message message)
    {
        Company company = companyRepository.getById(message.getCompany().getId());
        Student student = studentRepository.getById(message.getStudent().getId());
        Post post = postRepository.getById(message.getPost().getId());

        if (company != null && student != null && post != null)
        {
            Message newMessage = new Message(message.getBody(),company,student,post);
            return ResponseEntity.accepted().body(messageRepository.save(newMessage));
        }
        else
            return ResponseEntity.badRequest().body(null);
    }

    public  boolean function(Long id,String token)
    {
        final String username = jwtUtils.getUserNameFromJwtToken(token);
        return userRepository.getByUsername(username).getStudent() == id;
    }

    @PreAuthorize("hasRole('ENTREPRISE')")
    @PutMapping("/messages/{id}")
    public ResponseEntity update(@Valid @RequestBody Message message,@PathVariable Long id,@RequestHeader(name="Authorization") String tokenHeader)
    {
        Message message1 = messageRepository.getById(id);
        if (message1 != null)
        {
            final String token = tokenHeader.split(" ")[1];
            final String username = jwtUtils.getUserNameFromJwtToken(token);

            if(userRepository.getByUsername(username).getCompany() == message1.getCompany().getId())
            {
                message1.setBody(message.getBody());
                return ResponseEntity.accepted().body(messageRepository.save(message1));
            }

        }
        return ResponseEntity.badRequest().body(null);
    }

    @PreAuthorize("hasRole('ENTREPRISE') OR hasRole('ADMIN')")
    @DeleteMapping("/messages/{id}")
    public void destroy(@PathVariable Long id,@RequestHeader(name="Authorization") String tokenHeader)
    {
        Message message = messageRepository.getById(id);
        if (message != null)
        {
            final String token = tokenHeader.split(" ")[1];
            final String username = jwtUtils.getUserNameFromJwtToken(token);
            final ERole roleName = userRepository.getByUsername(username).getRoles().stream().findFirst().get().getName();

            if( (roleName == ERole.ROLE_ENTREPRISE && userRepository.getByUsername(username).getCompany() == message.getCompany().getId()) || roleName == ERole.ROLE_ADMIN )
                messageRepository.deleteById(id);
        }
    }


}
