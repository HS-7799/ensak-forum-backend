package com.forum.ensak.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="files")
public class DBFile {
    @Id
    /**@GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid",strategy="uuid2")**/
    private String id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;

    private String fileDownloadUri;


    public DBFile() {

    }

    public DBFile(String fileName, String fileType, byte[] data) {
        this.id = UUID.randomUUID().toString();
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(this.id)
                .toUriString();

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri=fileDownloadUri;
    }

}
