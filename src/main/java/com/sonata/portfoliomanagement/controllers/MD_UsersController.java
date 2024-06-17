package com.sonata.portfoliomanagement.controllers;

import java.util.List;

import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/user")
public class MD_UsersController {

    @Autowired
    private MD_UsersRepository usersRepo;

    @PostMapping("/save")
    public ResponseEntity<MD_Users> createUsers(@RequestBody MD_Users users) {
        MD_Users createduser = usersRepo.save(users);
        return new ResponseEntity<>(createduser, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<MD_Users>> getAllUsers() {
        List<MD_Users> allUsers = usersRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }
    @PutMapping("/update")
    public ResponseEntity<List<MD_Users>> updateUsers(@RequestBody List<MD_Users> updatedUsersList) {

        try {

            for (MD_Users updatedUsers : updatedUsersList) {
                int id = updatedUsers.getId();
                if (!usersRepo.existsById(id)) {
                    return ResponseEntity.notFound().build();
                }
            }

            List<MD_Users> updatedUsers = usersRepo.saveAll(updatedUsersList);
            return ResponseEntity.ok(updatedUsers);
        }

        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUsersByIds(@RequestBody List<Integer> userIds) {
        try {
            for (Integer userId : userIds) {
                if (!usersRepo.existsById(userId)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found with ID: " + userId);
                }
            }

            usersRepo.deleteAllById(userIds);
            return ResponseEntity.status(HttpStatus.OK).body("users with specified IDs have been deleted.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting users.");
        }
    }

}