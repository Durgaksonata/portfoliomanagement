package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.model.User;
import com.sonata.portfoliomanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
    }

//    @PostMapping("/checkUser")
//    public ResponseEntity<String> checkUser(@RequestBody User user) {
//        try {
//            String email = user.getEmail();
//            String password = user.getPassword();
//            // Check if user with provided email and password exists in the database
//            boolean userExists = userService.userExistsByEmailAndPassword(email, password);
//            if (userExists) {
//                return ResponseEntity.ok("User Logged in Successfully");
//            }
//            else {
//                return ResponseEntity.ok("Data does not exist, Create New Account");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check user: " + e.getMessage());
//        }
//    }


    @PostMapping("/checkUser")
    public ResponseEntity<String> checkUser(@RequestBody User user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = userService.userExistsByEmail(email);
            if (!emailExists) {
                return ResponseEntity.ok("email provided is incorrect");
            }
            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = userService.verifyPassword(email, password);
            if (passwordCorrect) {
                return ResponseEntity.ok("User logged in successfully");
            } else {
                return ResponseEntity.ok("Password provided is incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check user: " + e.getMessage());
        }
    }



    //
//    @PutMapping("/users/{id}")
//    public ResponseEntity<String> updateUser(@PathVariable("id") int id, @RequestBody User updatedUser) {
//        try {
//            // Retrieve the user by id
//            User existingUser = userService.getUserById(id);
//            if (existingUser == null) {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//            }
//
//            // Update the user with the new information
//            existingUser.setPassword(updatedUser.getPassword());
//            existingUser.setEmail(updatedUser.getEmail());
//            // Set other properties as needed
//
//            userService.saveUser(existingUser); // Save the updated user
//
//            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
//        }
//    }
    @PutMapping("/users/{email}")
    public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody User updatedUser) {
        try {
            // Retrieve the user by email
            User existingUser = userService.getUserByEmail(email);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Update the user with the new information
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setEmail(updatedUser.getEmail());
            // Set other properties as needed

            userService.saveUser(existingUser); // Save the updated user

            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        try {
            User user = userService.getUserById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            if (!users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.noContent().build(); // No users found
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
