package com.forum.ensak.controllers;

import com.forum.ensak.models.DBFile;
import com.forum.ensak.repository.DBFileRepository;
import com.forum.ensak.response.MessageResponse;
import com.forum.ensak.services.DBFileStorageService;
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

@RestController
public class FileController {
    @Autowired
    private DBFileRepository dbFileRepository;
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private DBFileStorageService dbFileStorageService;
    @PostMapping("/uploadFile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        if(file != null)
        {
            DBFile dbFile = dbFileStorageService.storeFile(file);
            return ResponseEntity.ok(dbFile.getFileDownloadUri());
        } else {
            String errors = "Resume is required";
            return ResponseEntity.badRequest().body(new MessageResponse(errors));
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
