package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    // controller --> service --> repository

    @Autowired
    private UserRepository userRepository;

    public void saveEntry(User User) {
        userRepository.save(User);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> getById(ObjectId id) {
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.findById(id).ifPresent(userRepository::delete);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
