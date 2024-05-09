package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.UserRepository;
import com.sonata.portfoliomanagement.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;

@RestController
@RequestMapping("UserLogin")
public class UserController {

    @Autowired
    private UserRepository userrepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @PostMapping("/save")
//    public UserLogin loginUser(@RequestBody UserLogin user) throws Exception {
//        String tempEmail = user.getEmail();
//        String tempPass = user.getPassword();
//        UserLogin userObj = null;
//        if (tempEmail != null && tempPass != null) {
//            userObj = userrepository.findByEmailAndPassword(tempEmail, tempPass);
//        }
//        if (userObj == null) {
//            throw new Exception("bad credentials");
//        }
//        return userObj;
//    }

    @PostMapping("/registerUser")
    public ResponseEntity<?> registerUser(@RequestBody UserLogin userRegistrationRequest) {
        String email = userRegistrationRequest.getEmail();
        String password = userRegistrationRequest.getPassword();

        // Perform validation on email and password if needed

        //Hashed password
        String hashedPassword = hashPassword(password);

        // Create a new User object with the provided email and password
        UserLogin newUser = new UserLogin();
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);

        // Save the new user to the database
        UserLogin savedUser;
        try {
            savedUser = userrepository.save(newUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save user: " + e.getMessage());
        }

        // Return the saved user object in the response
        return ResponseEntity.ok(savedUser);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("WWW-Authenticate", "Bearer realm=\"example\"");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body("Unauthorized: " + ex.getMessage());
    }

    private String hashPassword(String password) {
        try {
            // Create MessageDigest instance for SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Add password bytes to digest
            md.update(password.getBytes());
            // Get the hash's bytes
            byte[] bytes = md.digest();
            // This bytes[] has bytes in decimal format;
            // Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            }
            // Get complete hashed password in hexadecimal format
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

//    @PutMapping("/updateuser")
//    public ResponseEntity<?> updateuser(@RequestBody UserLogin updateduser) {
//        try {
//            int id = updateduser.getId(); // Assuming there's an ID field in AdminModel
//            String newEmail = updateduser.getEmail();
//            String newPassword = updateduser.getPassword();
//            UserLogin existinguser = userrepository.findById(id)
//                    .orElseThrow(() -> new NoSuchElementException("user not found"));
//
//            // Update only if new email and password are provided
//            if (newEmail != null && newPassword != null) {
//                existinguser.setEmail(newEmail);
//                existinguser.setPassword(newPassword);
//                UserLogin updatedUserModel = userrepository.save(existinguser);
//                return ResponseEntity.ok(updatedUserModel);
//            } else {
//                throw new Exception("New email and password are required for update");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

//    @PutMapping("/updateuser")
//    public ResponseEntity<?> updateuser(@RequestBody UserLogin updateduser) {
//        try {
//            int id = updateduser.getId(); // Assuming there's an ID field in AdminModel
//            String newEmail = updateduser.getEmail();
//            String newPassword = updateduser.getPassword();
//            Optional<UserLogin> optionalExistingUser = Optional.ofNullable(userrepository.findById(id));
//            // Check if user exists
//            if (optionalExistingUser.isPresent()) {
//                UserLogin existingUser = optionalExistingUser.get();
//                // Update only if new email and password are provided
//                if (newEmail != null && newPassword != null) {
//                    existingUser.setEmail(newEmail);
//                    existingUser.setPassword(newPassword);
//                    UserLogin updatedUserModel = userrepository.save(existingUser);
//                    return ResponseEntity.ok(updatedUserModel);
//                } else {
//                    throw new Exception("New email and password are required for update");
//                }
//            } else {
//                throw new NoSuchElementException("User not found");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

    @PutMapping("/updateUser/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") int id, @RequestBody UserLogin userUpdateRequest) {
        try {
            String newEmail = userUpdateRequest.getEmail();
            String newPassword = userUpdateRequest.getPassword();

            // Retrieve the existing user from the database
            Optional<UserLogin> optionalExistingUser = userrepository.findById(id);

            // Check if user exists
            if (optionalExistingUser.isPresent()) {
                UserLogin existingUser = optionalExistingUser.get();

                // Update email if new email is provided
                if (newEmail != null) {
                    existingUser.setEmail(newEmail);
                }

                // Update password if new password is provided
                if (newPassword != null) {
                    existingUser.setPassword(newPassword);
                }

                // Save the updated user to the database
                UserLogin updatedUser = userrepository.save(existingUser);

                // Return the updated user object in the response
                return ResponseEntity.ok(updatedUser);
            } else {
                throw new NoSuchElementException("User not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

//    @PutMapping("/updateUser/{email}")
//    public ResponseEntity<?> updateuser(@PathVariable("email") String email, @RequestBody UserLogin userUpdateRequest) {
//        try {
//            String newPassword = userUpdateRequest.getPassword();
//
//            // Retrieve the existing user from the database
//            Optional<UserLogin> optionalExistingUser = userrepository.findByEmail(email);
//
//            // Check if user exists
//            if (optionalExistingUser.isPresent()) {
//                UserLogin existingUser = optionalExistingUser.get();
//
//                // Update email if new email is provided
//
//
//                // Update password if new password is provided
//                if (newPassword != null) {
//                    existingUser.setPassword(newPassword);
//                }
//
//                // Save the updated user to the database
//                UserLogin updatedUser = userrepository.save(existingUser);
//
//                // Return the updated user object in the response
//                return ResponseEntity.ok(updatedUser);
//            } else {
//                throw new NoSuchElementException("User not found");
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }

}
