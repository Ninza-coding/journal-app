package com.techn.journalapp.controller;

import com.techn.journalapp.api.response.WeatherResponse;
import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.UserRepository;
import com.techn.journalapp.service.UserService;
import com.techn.journalapp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String userName=authentication.getName();

        User userInDb = userService.findByUserName(userName);

            userInDb.setUserName(user.getUserName());
            userInDb.setPassword(user.getPassword());
            userService.saveNewUser(userInDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        userRepository.deleteByUserName(userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greeting(){
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        WeatherResponse weatherResponse = weatherService.getWeather("Mumbai");
        String greetings ="";
        if(weatherResponse!=null){
            greetings=" Weather feels like "+  weatherResponse.getCurrent().getFeelslike();
        }
        System.out.println(greetings);
            return new ResponseEntity<>("Hi "+ userName+ greetings,HttpStatus.OK);
        }
}
