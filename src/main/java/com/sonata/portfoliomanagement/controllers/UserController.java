package com.sonata.portfoliomanagement.controllers;
import com.sonata.portfoliomanagement.model.User;
import com.sonata.portfoliomanagement.services.UserService;
import org.mindrot.jbcrypt.BCrypt;
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



    @PostMapping("/checkUserAndReturnResponse")
    public ResponseEntity<Map<String, Object>> checkUserAndReturnResponse(@RequestBody User user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = userService.userExistsByEmail(email);
            if (!emailExists) {
                // User with the provided email does not exist
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Email provided is incorrect"));
            }

            // Get the existing user
            User existingUser = userService.getUserByEmail(email);

            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = userService.verifyPassword(email, password);
            if (passwordCorrect) {
                // Check if isFirstLogin is true
                boolean isFirstLogin = existingUser.isFirstLogin();
                if (isFirstLogin) {
                    // Update isFirstLogin to false (only in memory)
                    existingUser.setFirstLogin(false);
                    // Prepare the response JSON
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Email provided is correct");
                    response.put("isFirstLogin", isFirstLogin);

                    // Return the response without saving the user
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // isFirstLogin is already false
                    Map<String, Object> response = new HashMap<>();
                    response.put("Status", "Email provided is correct");
                    response.put("isFirstLogin", isFirstLogin);
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // Password provided is incorrect
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("Status", "Password provided is incorrect");
                errorResponse.put("isFirstLogin", existingUser.isFirstLogin());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "Failed to check user: " + e.getMessage()));
        }
    }





@PutMapping("/users/{email}")
public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody Map<String, String> passwordMap) {
    try {
        // Retrieve the user by email
        User existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Get the old and new passwords from the request body
        String oldPassword = passwordMap.get("oldPassword");
        String newPassword = passwordMap.get("newPassword");

        // Retrieve the hashed password associated with the user's email from the database
        String hashedPasswordFromDB = existingUser.getPassword();

        // Check if the provided old password matches the hashed password stored in the database
        if (!BCrypt.checkpw(oldPassword, hashedPasswordFromDB)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect old password");
        }

        // Generate a new salt and hash the new password with it
        String newSalt = BCrypt.gensalt();
        String newHashedPassword = BCrypt.hashpw(newPassword, newSalt);

        // Update the user's password with the new hashed password
        existingUser.setPassword(newHashedPassword);
        userService.saveUser(existingUser); // Save the updated user

        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update password: " + e.getMessage());
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