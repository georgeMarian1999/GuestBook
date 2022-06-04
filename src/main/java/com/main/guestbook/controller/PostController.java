package com.main.guestbook.controller;

import com.main.guestbook.tableservice.ServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController("/post")
public class PostController {

    @Autowired
    private ServiceClient serviceClient;

    @RequestMapping(method = RequestMethod.GET, path = "/name")
    public String getPostTableName() {
        return serviceClient.getTableName();
    }


}
