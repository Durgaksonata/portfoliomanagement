package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.model.User;
import com.sonata.portfoliomanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // Check if a user with the same email already exists
            if (userService.userExistsByEmail(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with email: " + user.getEmail());
            }

            // Set isFirstLogin based on the provided value or default it to true
            boolean isFirstLogin = user.isFirstLogin();
            if (!user.isFirstLogin()) {
                isFirstLogin = true; // Defaulting isFirstLogin to true if not provided
            }

            // Set the isFirstLogin value in the user object
            user.setFirstLogin(isFirstLogin);

            // Save the user
            userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
        }
    }


    @PostMapping("/checkUser")
    public ResponseEntity<String> checkUser(@RequestBody User user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = userService.userExistsByEmail(email);
            if (!emailExists) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email provided is incorrect");
            }
            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = userService.verifyPassword(email, password);
            if (passwordCorrect) {
                return ResponseEntity.status(HttpStatus.OK).body("User logged in successfully");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password provided is incorrect");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to check user: " + e.getMessage());
        }
    }


    //Check whether user exists and return me the response for it-->
    @PostMapping("/checkUserAndReturnResponse")
    public ResponseEntity<Map<String, Object>> checkUserAndReturnResponse(@RequestBody User user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = userService.userExistsByEmail(email);
            if (!emailExists) {
                // User with the provided email does not exist
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("response", "Email provided is incorrect"));
            }

            // Get the existing user
            User existingUser = userService.getUserByEmail(email);

            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = userService.verifyPassword(email, password);
            if (passwordCorrect) {
                // Check if isFirstLogin is true
                boolean isFirstLogin = existingUser.isFirstLogin();
                if (isFirstLogin) {
                    // Update isFirstLogin to false and save the user
                    existingUser.setFirstLogin(false);
                    userService.saveUser(existingUser); // Update isFirstLogin in the database
                }

                // Prepare the response JSON
                Map<String, Object> response = new HashMap<>();
                response.put("response", "Email provided is correct");
                response.put("isFirstLogin", isFirstLogin);

                // Return the response
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                // Password provided is incorrect
                // Include isFirstLogin status in the response
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("response", "Password provided is incorrect");
                errorResponse.put("isFirstLogin", existingUser.isFirstLogin());

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("response", "Failed to check user: " + e.getMessage()));
        }
    }



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