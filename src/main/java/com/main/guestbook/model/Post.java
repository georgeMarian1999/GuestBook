package com.main.guestbook.model;

import com.azure.data.tables.implementation.TablesImpl;
import org.apache.tomcat.jni.Local;

import java.io.Serializable;
import java.time.LocalDateTime;


public class Post  implements Serializable {
    private String partitionKey;
    private String rowKey;
    private String title;
    private String text;

    private byte[] file;
    private LocalDateTime date;

    public Post() {

    }

    public Post(String partitionKey, String rowKey, String title, String text, byte[] file, LocalDateTime date) {
        this.partitionKey = partitionKey;
        this.rowKey = rowKey;
        this.title = title;
        this.text = text;
        this.file = file;
        this.date = date;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public Post(String title, String text, LocalDateTime date) {
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public Post(String partitionKey, String rowKey, String title, String text, LocalDateTime date) {
        this.partitionKey = partitionKey;
        this.rowKey = rowKey;
        this.title = title;
        this.text = text;
        this.date = date;
    }

    public String getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(String partitionKey) {
        this.partitionKey = partitionKey;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
