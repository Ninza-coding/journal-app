package com.techn.journalapp.controller;

import com.techn.journalapp.entity.JournalEntry;
import com.techn.journalapp.entity.User;
import com.techn.journalapp.service.JournalEntryService;
import com.techn.journalapp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllUserEntries() {
        String userName=SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);
        if(user!=null) {
            List<JournalEntry> all = user.getJournalEntries();
            if (all != null && !all.isEmpty()) {
                return new ResponseEntity<>(all, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            String userName= SecurityContextHolder.getContext().getAuthentication().getName();
            journalEntryService.save(myEntry, userName);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{objectId}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId objectId) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
            if(journalEntry.isPresent()){ //map() is taking Function orElseGet() is taking the Supplier
            return  new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
        }
            }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
//            Optional<JournalEntry> journalEntry = journalEntryService.findById(objectId);
//            return journalEntry.map(entry -> new ResponseEntity<>(entry, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//
//        }
//        return null;
//        if(journalEntry.isPresent()){ map() is taking Function orElseGet() is taking the Supplier
//            return  new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    @DeleteMapping("id/{objectId}")
    public ResponseEntity<?> deleteById(@PathVariable ObjectId objectId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();

        boolean removed = journalEntryService.deleteById(objectId, userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{objectId}")
    public ResponseEntity<JournalEntry> update(@PathVariable ObjectId objectId,
                                               @RequestBody JournalEntry newEntry) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(objectId)).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            JournalEntry old = journalEntryService.findById(objectId).orElse(null);
            if (old != null) {
                old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
                old.setContent(newEntry.getContent() != null && !newEntry.getContent().equals("") ? newEntry.getContent() : old.getContent());
                journalEntryService.save(old);
                return new ResponseEntity<>(old, HttpStatus.CREATED);
            }

        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

}