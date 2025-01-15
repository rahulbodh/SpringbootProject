package net.engineeringdigest.journalApp.controller;


import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    // controller --> service --> repository

    @Autowired
    private JournalEntryService journalEntryService;
    
    @Autowired
    private UserService userService;
    

    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        List<JournalEntry> allEntries = user.getJournalEntry();
        if(allEntries != null && !allEntries.isEmpty()) {
            return new ResponseEntity<>(allEntries , HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("{userName}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry , @PathVariable String userName) {
        try {
            User user = userService.findByUserName(userName);
            journalEntryService.saveEntry(myEntry , userName);
            return new ResponseEntity<>(myEntry , HttpStatus.CREATED);
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("id/{myId}")
    public ResponseEntity<JournalEntry> getJournalEntryById(@PathVariable ObjectId myId) {
        Optional<JournalEntry> journalEntry = journalEntryService.getById(myId);
        if(journalEntry.isPresent()) {
            return new ResponseEntity<>(journalEntry.get() , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{userName}/{myId}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myId , @RequestBody String userName) { // ? - wild card pattern here , we can wrap another class object to the response entity
         journalEntryService.deleteById(myId , userName);
         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllEntries() {
        journalEntryService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("id{userName}/{myId}")
    public ResponseEntity<?> updateJournalEntryById(@PathVariable ObjectId myId , @RequestBody JournalEntry myEntry , @PathVariable String userName) {
        JournalEntry oldEntry = journalEntryService.getById(myId).orElse(null);
        if (oldEntry != null) {
          oldEntry.setTitle(myEntry.getTitle() != null || myEntry.getTitle().equals("") ? myEntry.getTitle() : oldEntry.getTitle());
          oldEntry.setContent(myEntry.getContent() != null || myEntry.getContent().equals("") ? myEntry.getContent() : oldEntry.getContent());
            journalEntryService.saveEntry(oldEntry);
            return new ResponseEntity<>(oldEntry , HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




}
