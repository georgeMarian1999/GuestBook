package com.main.guestbook.controller;

import com.azure.data.tables.models.TableServiceException;
import com.main.guestbook.model.Post;
import com.main.guestbook.tableservice.AzureClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.MultipartConfigElement;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("post")
public class PostController {

    @Autowired
    private AzureClient azureClient;


    @RequestMapping(method = RequestMethod.GET, path = "/table/name")
    public String getPostTableName() {
        return azureClient.getTableName();
    }

    @GetMapping(path = "/{rowKey}")
    public Post getPostById(@PathVariable String rowKey) {
        return azureClient.getPostById(rowKey);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public List<Post> getALlPosts() {
        try {
            return azureClient.getAllPosts();
        }catch (TableServiceException ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    @DeleteMapping(path = "/{rowKey}")
    public void deletePost(@PathVariable String rowKey) {
        try {
            azureClient.deletePost(rowKey);
        }catch (TableServiceException ex) {
            ex.printStackTrace();

        }
    }

    @PostMapping(path = "/")
    public void addPost(@RequestParam("title") String title, @RequestParam("text") String text, @RequestParam("image") MultipartFile image) {
        try {
            azureClient.addPost(title, text, image);
        }catch (TableServiceException ex) {
            ex.printStackTrace();
        }
    }


}
