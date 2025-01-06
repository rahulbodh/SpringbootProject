package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    // controller --> service --> repository

    @Autowired
    private JournalEntryService journalEntryService;

    @GetMapping
    public List<JournalEntry> getAllJournalEntries() {
        return journalEntryService.getAll();
    }


    @PostMapping
    public boolean createEntry(@RequestBody JournalEntry myEntry){
        myEntry.setDate(LocalDateTime.now());
        journalEntryService.saveEntry(myEntry);
        return true;
    }

    @GetMapping("id/{myId}")
    public JournalEntry getJournalEntryById(@PathVariable ObjectId myId) {
        return journalEntryService.getById(myId).orElse(null);
    }

    @DeleteMapping("id/{myId}")
    public void deleteJournalEntryById(@PathVariable ObjectId myId) {
         journalEntryService.deleteById(myId);
    }

    @DeleteMapping("/all")
    public boolean deleteAllEntries() {
        journalEntryService.deleteAll();
        return true;
    }

    @PutMapping("id/{myId}")
    public JournalEntry updateJournalEntryById(@PathVariable ObjectId myId , @RequestBody JournalEntry myEntry) {
        JournalEntry oldEntry = journalEntryService.getById(myId).orElse(null);
        if (oldEntry != null) {
          oldEntry.setTitle(myEntry.getTitle() != null || myEntry.getTitle().equals("") ? myEntry.getTitle() : oldEntry.getTitle());
          oldEntry.setContent(myEntry.getContent() != null || myEntry.getContent().equals("") ? myEntry.getContent() : oldEntry.getContent());
        }
        journalEntryService.saveEntry(oldEntry);
        return oldEntry;
    }




}
