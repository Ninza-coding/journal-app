package com.techn.journalapp.service;

import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean saveNewUser(User user){
        try {
            user.setPassword(passwordEncoder.encode(user.getUserName()));
            user.setRoles(Arrays.asList("USER"));
           // user.setJournalEntries(new ArrayList<>()); // intilizing not null in the List.
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.debug("Debug Occurred {} :", user.getUserName());
            log.error("Error Occurred {} :", user.getUserName());

            log.trace("Trace Occurred {} :", user.getUserName());
            log.warn("Warn Occurred {} :", user.getUserName());
            return false;
        }
    }

    public void saveAdminUser(User user){
        user.setPassword(passwordEncoder.encode(user.getUserName()));
        user.setRoles(Arrays.asList("ADMIN"));
        userRepository.save(user);
    }

    public void saveInUser(User user){userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId objectId){
        return  userRepository.findById(objectId);
    }

    public void deleteById(ObjectId objectId){
        userRepository.deleteById(objectId);
    }

    public User findByUserName(String userName){
        return userRepository.findByUserName(userName);

    }


}
