package com.forum.ensak.controllers;

import com.forum.ensak.models.ActivityArea;
import com.forum.ensak.repository.ActivityAreaRepository;
import com.forum.ensak.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ActivityAreaController {


    private ActivityAreaRepository activityareaRepository;

    public ActivityAreaController(ActivityAreaRepository activityareaRepository)
    {
        this.activityareaRepository = activityareaRepository;
    }

    @GetMapping("/activityareas")
    public List<ActivityArea> index()
    {
        return activityareaRepository.findAll();
    }

    @GetMapping("/activityareas/{id}")
    public ResponseEntity<ActivityArea> show(@Valid @PathVariable Long id)
    {
        ActivityArea activityarea = activityareaRepository.getById(id);
        if(activityarea != null)
        {
            return new ResponseEntity<ActivityArea>(activityarea, HttpStatus.OK);
        }
        return new ResponseEntity<ActivityArea>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/activityareas")
    public ResponseEntity<? extends Object> store(@Valid @RequestBody ActivityArea newActivityArea)
    {
        if(activityareaRepository.existsByName(newActivityArea.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already used"));
        }

        ActivityArea activityarea = activityareaRepository.save(newActivityArea);
        return ResponseEntity.accepted().body(activityarea);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activityareas/{id}")
    public ResponseEntity update(@Valid @RequestBody ActivityArea newActivityArea,@PathVariable Long id)
    {
        if(activityareaRepository.existsByName(newActivityArea.getName()))
        {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("This name is already in use!"));
        }

        ActivityArea s = activityareaRepository.findById(id)
                .map(activityarea -> {
                    activityarea.setName(newActivityArea.getName());
                    activityarea.setLabel(newActivityArea.getLabel());
                    activityarea.setCompanies(newActivityArea.getCompanies());
                    return activityareaRepository.save(activityarea);
                })
                .orElseGet(() -> {
                    newActivityArea.setId(id);
                    return activityareaRepository.save(newActivityArea);
                });
        return ResponseEntity.accepted().body(s);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/activityareas/{id}")
    public ResponseEntity destroy(@Valid @PathVariable Long id)
    {
        ActivityArea activityarea = activityareaRepository.getById(id);
        if(activityarea != null)
        {
            if(activityarea.getCompanies().size() != 0)
                return ResponseEntity.badRequest().body(new MessageResponse("Some companies belong to this activityarea"));
            this.activityareaRepository.deleteById(id);
        }

        return ResponseEntity.ok(null);
    }

}
