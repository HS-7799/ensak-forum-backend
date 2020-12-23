package com.forum.ensak.controllers;

import com.forum.ensak.models.Level;
import com.forum.ensak.repository.LevelRepository;
import com.forum.ensak.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LevelController {

    @Autowired
    private LevelRepository levelRepository;

    public LevelController(LevelRepository levelRepository)
    {
        this.levelRepository = levelRepository;
    }
    @GetMapping("/levels")
    public List<Level> index()
    {
        return levelRepository.findAll();
    }

    @GetMapping("/levels/{id}")
    public ResponseEntity<Level> show(@Valid @PathVariable Long id)
    {
        Level level = levelRepository.getById(id);
        if(level != null)
        {
            return new ResponseEntity<Level>(level,HttpStatus.OK);
        }
        return new ResponseEntity<Level>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/levels")
    public ResponseEntity<? extends Object> store(@Valid @RequestBody Level newLevel)
    {
        if(levelRepository.existsByName(newLevel.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already in use!"));
        }

        Level level = levelRepository.save(newLevel);
        return ResponseEntity.accepted().body(level);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/levels/{id}")
    public ResponseEntity update(@Valid @RequestBody Level newLevel,@PathVariable Long id)
    {
        if(levelRepository.existsByName(newLevel.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already in use!"));
        }

        Level l = levelRepository.findById(id)
                .map(level -> {
                    level.setName(newLevel.getName());
                    level.setLabel(newLevel.getLabel());
                    level.setStudents(newLevel.getStudents());
                    return levelRepository.save(level);
                })
                .orElseGet(() -> {
                    newLevel.setId(id);
                    return levelRepository.save(newLevel);
                });
        return ResponseEntity.accepted().body(l);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/levels/{id}")
    public ResponseEntity destroy(@Valid @PathVariable Long id)
    {
        Level level = levelRepository.getById(id);
        if(level != null)
        {
            if(level.getStudents().size() != 0)
                return ResponseEntity.badRequest().body(new MessageResponse("Some students have this level"));
            this.levelRepository.deleteById(id);
        }

        return ResponseEntity.accepted().body(null);

    }


}
