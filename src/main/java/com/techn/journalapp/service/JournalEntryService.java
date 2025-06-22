package com.techn.journalapp.service;

import com.techn.journalapp.entity.JournalEntry;
import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private UserService userService;


    @Transactional
    public void save(JournalEntry journalEntry, String userName){
        try {
            User userInDb = userService.findByUserName(userName);
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            userInDb.getJournalEntries().add(saved);
            userService.saveInUser(userInDb);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Illeagel Entry "+e);
        }
    }

    public void save(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getEntries(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId objectId){
        return  journalEntryRepository.findById(objectId);
    }

    @Transactional
    public boolean deleteById(ObjectId objectId, String userName){
        boolean removed= false;
        try {
            User userInDb = userService.findByUserName(userName);

            // 1 . Remove the journal entry from User's List<JournalEntry>
            removed = userInDb.getJournalEntries().removeIf((x) -> x.getId().equals(objectId));

            if(removed) {
                // 2. Delete the journal entry from DB
                journalEntryRepository.deleteById(objectId);

                // 3. Save updated user
                userService.saveInUser(userInDb);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("Something went wrong"+e);
        }
        return removed;
    }

}
