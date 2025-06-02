package com.techn.journalapp.service;

import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveEntries(User user){
        userRepository.save(user);
    }

    public List<User> getEntries(){
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
