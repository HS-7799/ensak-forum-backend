package com.forum.ensak.controllers;

import com.forum.ensak.models.DBFile;
import com.forum.ensak.models.Student;
import com.forum.ensak.repository.DBFileRepository;
import com.forum.ensak.repository.StudentRepository;
import com.forum.ensak.response.MessageResponse;
import com.forum.ensak.services.DBFileStorageService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
public class FileController {
    @Autowired
    private DBFileRepository dbFileRepository;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DBFileStorageService dbFileStorageService;

    @PreAuthorize("hasRole('ETUDIANT')")
    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        String errors = "";

        if (!file.getContentType().equals("application/pdf")){
            errors += "Please make sure you upload pdf file";
        }

        if (file.isEmpty()){
            errors += "  make sure you select file";
        }

        if(errors.length() > 0)
        {
            return ResponseEntity.badRequest().body(new MessageResponse(errors));
        }

        DBFile dbFile = dbFileStorageService.storeFile(file);
        return ResponseEntity.ok(dbFile.getFileDownloadUri());
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @GetMapping("/file/{id}")
    public ResponseEntity<DBFile> show(@Valid @PathVariable Long id)
    {
        //DBFile file = dbFileRepository.getById(id);
        Student student = studentRepository.getById(id);

        DBFile dbfile=dbFileRepository.getByFileDownloadUri(student.getFileDownloadUri());

        if(dbfile != null)
        {
            return new ResponseEntity<DBFile>(dbfile, HttpStatus.OK);
        }
        return new ResponseEntity<DBFile>(HttpStatus.NOT_FOUND);
    }

    @PreAuthorize("hasRole('ETUDIANT')")
    @DeleteMapping("/file/{id}")
    public void destroy(@Valid @PathVariable String id)
    {
        //DBFile file = dbFileRepository.getById(id);

        DBFile dbfile=dbFileRepository.getByFileDownloadUri(id);

        if(dbfile != null)
        {
            this.dbFileRepository.deleteById(id);
        }

    }

    @GetMapping("/downloadFile/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        // Load file from database
        DBFile dbFile = dbFileStorageService.getFile(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
}
