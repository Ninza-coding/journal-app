package com.techn.journalapp.controller;

import com.techn.journalapp.entity.User;
import com.techn.journalapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private UserService userService;

    @GetMapping("/health-check")
    public String helathCheck(){
        return "Ok";
    }

    @PostMapping("create-user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        boolean saved = userService.saveNewUser(user);
        if(saved){
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


}
