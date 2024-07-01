package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.MD_AdminRepositoy;
import com.sonata.portfoliomanagement.model.MD_Admin;
import com.sonata.portfoliomanagement.model.MD_Users;
import com.sonata.portfoliomanagement.services.AESUtil;
import com.sonata.portfoliomanagement.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/admin")
public class MD_AdminController {

    @Autowired
    public MD_AdminController(AdminService adminService) {
        this.adminService = (AdminService) adminService;
    }

    @Autowired
    private MD_AdminRepositoy adminRepo;
    @Autowired
    private AdminService adminService;


    @GetMapping("/get")
    public ResponseEntity<List<MD_Admin>> getAllUsers() {
        List<MD_Admin> allUsers = adminRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }


    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody MD_Admin user) {
        Map<String, Object> response = new HashMap<>();

        // Validate input: ensure at least one of first name or last name is provided
        if ((user.getFirstName() == null || user.getFirstName().trim().isEmpty()) &&
                (user.getLastName() == null || user.getLastName().trim().isEmpty())) {
            response.put("message", "At least one of first name or last name must be provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if a user with the same email already exists
        if (adminService.userExistsByEmail(user.getEmail())) {
            response.put("message", "User already exists with email: " + user.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

//            // Set isFirstLogin based on the provided value or default it to true
        boolean isFirstLogin = user.isFirstLogin();
        if (!user.isFirstLogin()) {
            isFirstLogin = true; // Defaulting isFirstLogin to true if not provided
        }

        // Set the isFirstLogin value in the user object
        user.setFirstLogin(isFirstLogin);


        try {
            // Set the default password and encrypt it
            String defaultPassword = "Sonata@123";
            // Encrypt the password
            String encryptedPassword = AESUtil.encrypt(defaultPassword);
            user.setPassword(encryptedPassword);

            // Save the new user if no conflicts are found
            MD_Admin createdUser = adminRepo.save(user);


            // Prepare the response with a success message and the created user
            response.put("message", "User with name " + createdUser.getFirstName() + " " +
                    createdUser.getLastName() + " and roles: " + createdUser.getRole() + " saved successfully.");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Failed to create user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Admin updatedUser) {
        // Validate input
        if ((updatedUser.getFirstName() == null || updatedUser.getFirstName().trim().isEmpty()) &&
                (updatedUser.getLastName() == null || updatedUser.getLastName().trim().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "At least one of first name or last name must be provided"));
        }

        Optional<MD_Admin> userOptional = adminRepo.findById(updatedUser.getId());

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }

        // Check for duplicates
        List<MD_Admin> duplicateUsers = adminRepo.findByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName());
        for (MD_Admin user : duplicateUsers) {
            if (user.getId() != (updatedUser.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message", "Duplicate entry: User with first name '"
                                + updatedUser.getFirstName() + "' and last name '" + updatedUser.getLastName() + "' already exists."));
            }
        }

        MD_Admin existingUser = userOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        // Update roles first
        if (!existingUser.getRole().equals(updatedUser.getRole())) {
            String oldRole = existingUser.getRole();
            existingUser.setRole(updatedUser.getRole());
            updateMessage.append("Role updated from '").append(oldRole).append("' to '").append(updatedUser.getRole()).append("'. ");
            isUpdated = true;
        }

        // Update the first name if changed
        if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().trim().isEmpty() &&
                !existingUser.getFirstName().equals(updatedUser.getFirstName())) {
            String oldFirstName = existingUser.getFirstName();
            existingUser.setFirstName(updatedUser.getFirstName());
            updateMessage.append("First name updated from '").append(oldFirstName).append("' to '").append(updatedUser.getFirstName()).append("'. ");
            isUpdated = true;
        }

        // Update the last name if changed
        if (updatedUser.getLastName() != null && !updatedUser.getLastName().trim().isEmpty() &&
                !existingUser.getLastName().equals(updatedUser.getLastName())) {
            String oldLastName = existingUser.getLastName();
            existingUser.setLastName(updatedUser.getLastName());
            updateMessage.append("Last name updated from '").append(oldLastName).append("' to '").append(updatedUser.getLastName()).append("'. ");
            isUpdated = true;
        }

        // Update the email if changed
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            String oldEmail = existingUser.getEmail();
            existingUser.setEmail(updatedUser.getEmail());
            updateMessage.append("Email updated from '").append(oldEmail).append("' to '").append(updatedUser.getEmail()).append("'. ");
            isUpdated = true;
        }

        if (isUpdated) {
            // Save the updated user
            adminRepo.save(existingUser);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("message", updateMessage.toString());
            response.put("updatedUser", existingUser);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }
    }







    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUsersByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedUserNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Admin> userOptional = adminRepo.findById(id);
            if (userOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Admin user = userOptional.get();
                deletedUserNames.add(user.getFirstName() + " " + user.getLastName());
                adminRepo.deleteById(id);
            }
        }

        Map<String, Object> response = new HashMap<>();
        if (!notFoundIds.isEmpty()) {
            response.put("message", "No users found with IDs: " + notFoundIds.toString());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("message", "Users: " + String.join(", ", deletedUserNames) + " deleted successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    @PostMapping("/checkAdminAndReturnResponse")
    public ResponseEntity<Map<String, Object>> checkUserAndReturnResponse(@RequestBody MD_Admin user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = adminService.userExistsByEmail(email);
            if (!emailExists) {
                // User with the provided email does not exist
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("response", "Email provided is incorrect"));
            }
            // Get the existing user
            MD_Admin existingUser = adminService.getUserByEmail(email);

            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = adminService.verifyPassword(email, password);
            //System.out.println(passwordCorrect);
            if (passwordCorrect) {
                // Check if isFirstLogin is true
                boolean isFirstLogin = existingUser.isFirstLogin();
                //System.out.println(isFirstLogin);
                if (isFirstLogin) {
                    // Update isFirstLogin to false and save the user
                    existingUser.setFirstLogin(false);
                    adminService.saveUser(existingUser);

                    // Prepare the response JSON
                    Map<String, Object> response = new HashMap<>();
                    response.put("response", "Email provided is correct");
                    response.put("isFirstLogin", true); // Indicate that it was the first login
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    // isFirstLogin is already false
                    Map<String, Object> response = new HashMap<>();
                    response.put("response", "Email provided is correct");
                    response.put("isFirstLogin", false); // Indicate that it is not the first login
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                }
            } else {
                // Password provided is incorrect
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("response", "Password provided is incorrect");
                errorResponse.put("isFirstLogin", existingUser.isFirstLogin());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("response", "Failed to check user: " + e.getMessage()));
        }
    }




    @PutMapping("/{email}")
    public ResponseEntity<Map<String, String>> updateUser(@PathVariable("email") String email, @RequestBody Map<String, String> passwordMap) {
        try {
            // Retrieve the user by email
            MD_Admin existingUser = adminService.getUserByEmail(email);

            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Collections.singletonMap("response", "User with the provided email does not exist"));
            }

            // Get the old and new passwords from the request body
            String oldPassword = passwordMap.get("oldPassword");
            String newPassword = passwordMap.get("newPassword");

            // Decrypt the stored password
            String decryptedPasswordFromDB = AESUtil.decrypt(existingUser.getPassword());

            // Check if the provided old password matches the decrypted password from the database
            if (!Objects.equals(oldPassword, decryptedPasswordFromDB)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("response", "The old password provided is incorrect"));
            }

            // Check if the new password is the same as the old password
            if (Objects.equals(newPassword, oldPassword)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("response", "The new password cannot be the same as the old password"));
            }

            // Encrypt the new password and update the user's password
            String newEncryptedPassword = AESUtil.encrypt(newPassword);
            existingUser.setPassword(newEncryptedPassword);
            adminService.saveUser(existingUser);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Collections.singletonMap("response", "Password updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("response", "Failed to update password: " + e.getMessage()));
        }
    }





}
