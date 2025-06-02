package com.techn.journalapp.controller;

import com.techn.journalapp.entity.JournalEntry;
import com.techn.journalapp.entity.User;
import com.techn.journalapp.service.JournalEntryService;
import com.techn.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("{userName}")
    public ResponseEntity<List<JournalEntry>> getAllUserEntries(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        if(user!=null) {
            List<JournalEntry> all = user.getJournalEntries();
            if (all != null && !all.isEmpty()) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String userName) {
        try {
            journalEntryService.save(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{objectId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId objectId,@PathVariable String userName) {

        Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
        return  journalEntry.map(entry->new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(()->new ResponseEntity<>(HttpStatus.NOT_FOUND));
//        if(journalEntry.isPresent()){ map() is taking Function orElseGet() is taking the Supplier
//            return  new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{objectId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId objectId, @PathVariable String userName) {
        journalEntryService.deleteById(objectId, userName);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id/{userName}/{objectId}")
    public ResponseEntity<JournalEntry> update(@PathVariable ObjectId objectId,
                                               @RequestBody JournalEntry newEntry,
                                               @PathVariable String userName) {

        JournalEntry old = journalEntryService.findById(objectId).orElse(null);
        if (old != null) {
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
            journalEntryService.save(old);
            return new ResponseEntity<>(old, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}