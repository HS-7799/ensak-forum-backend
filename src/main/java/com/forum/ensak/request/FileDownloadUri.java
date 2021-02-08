package com.forum.ensak.request;

import javax.validation.constraints.NotBlank;

public class FileDownloadUri {

    @NotBlank(message = "fileDownloadUri is required field.")
    private String fileDownloadUri;


    public FileDownloadUri() {
    }

    public FileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }
}
