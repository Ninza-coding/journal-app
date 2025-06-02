package com.techn.journalapp.service;

import com.techn.journalapp.entity.JournalEntry;
import com.techn.journalapp.entity.User;
import com.techn.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

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
            userService.saveEntries(userInDb);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("illeagel Entry "+e);
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

    public void deleteById(ObjectId objectId, String userName){
        User userInDb = userService.findByUserName(userName);

        // 1. Delete the journal entry from DB


        // 2. Remove the journal entry from user's list manually
//        List<JournalEntry> updatedList = new ArrayList<>();
//        for (JournalEntry entry : userInDb.getJournalEntries()) {
//            if (!entry.getId().equals(objectId)) {
//                updatedList.add(entry);
//            }
//        }

//        userInDb.setJournalEntries(updatedList);
        userInDb.getJournalEntries().removeIf((x)-> x.getId().equals(objectId));

        journalEntryRepository.deleteById(objectId);

        // 3. Save updated user
        userService.saveEntries(userInDb);
    }

}
