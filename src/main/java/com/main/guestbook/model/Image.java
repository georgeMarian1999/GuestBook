package com.main.guestbook.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.Serializable;

public class Image implements Serializable {
    private String name;

    private byte[] file;

    public Image() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file2) {
        this.file = file2;
    }

    public Image(String postRowId, MultipartFile file) throws IOException {
        name = "image_rowKey-" + postRowId + ".jpg";

        this.file = file.getBytes();
    }

    public Image(String postRowId) {
        name = "image_rowKey-" + postRowId + ".jpg";
    }
}
