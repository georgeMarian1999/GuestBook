package com.main.guestbook.model;

import com.azure.data.tables.implementation.TablesImpl;

import java.util.Date;

public class Post  {
    private String partitionKey;
    private String rowKey;
    private String title;
    private String text;
    private String file;
    private Date date;

    public Post(String title, String text, Date date) {
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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
