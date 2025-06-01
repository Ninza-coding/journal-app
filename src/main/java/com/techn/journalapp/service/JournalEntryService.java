package com.techn.journalapp.service;

import com.techn.journalapp.entity.JournalEntry;
import com.techn.journalapp.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    public void save(JournalEntry journalEntry){
        journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getEntries(){
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId objectId){
        return  journalEntryRepository.findById(objectId);
    }

    public void deleteById(ObjectId objectId){
        journalEntryRepository.deleteById(objectId);
    }

}
