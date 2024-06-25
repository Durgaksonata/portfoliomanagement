package com.sonata.portfoliomanagement.controllers;

import java.util.*;

import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_Users;
import com.sonata.portfoliomanagement.services.AESUtil;
import com.sonata.portfoliomanagement.services.Md_UserService;
import com.sonata.portfoliomanagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class MD_UsersController {

       private final UserService userServices;


    @Autowired
    public MD_UsersController(UserService userService) {
        this.userServices = (UserService) userService;
    }

    @Autowired
    private MD_UsersRepository usersRepo;
    @Autowired
    private Md_UserService userService;

//    @PostMapping("/save")
//    public ResponseEntity<Map<String, Object>> createUser(@RequestBody MD_Users user) {
//        // Validate input
//        if ((user.getFirstName() == null || user.getFirstName().trim().isEmpty()) &&
//                (user.getLastName() == null || user.getLastName().trim().isEmpty())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Collections.singletonMap("message", "At least one of first name or last name must be provided"));
//        }
//        List<MD_Users> existingUsers = usersRepo.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());
//
//        if (!existingUsers.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT)
//                    .body(Collections.singletonMap("message", "Duplicate entries: Users with first name '"
//                            + user.getFirstName() + "' and last name '" + user.getLastName() + "' already exist."));
//        }
//
//        // Save the new user if no conflicts are found
//        MD_Users createdUser = usersRepo.save(user);
//
//        // Handle role assignments
//        for (String role : user.getRole()) {
//            if (role.equals("Delivery Director")) {
//                userService.createDeliveryDirector(user);
//            } else if (role.equals("Delivery Manager")) {
//                userService.createDeliveryManager(user);
//            } else if (role.equals("Project Manager")) {
//                userService.createProjectManager(user);
//            }
//            // Handle other roles if necessary
//        }
//
//        // Prepare the response with a success message and the created user
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", "User with name " + createdUser.getFirstName() + " " + createdUser.getLastName() +
//                " and roles: " + createdUser.getRole() + " saved successfully.");
//
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
//    }
//
//
//    @PostMapping("/users")
//    public ResponseEntity<String> createUsers(@RequestBody MD_Users user) {
//        try {
//            // Check if a user with the same email already exists
//            if (userServices.userExistsByEmail(user.getEmail())) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with email: " + user.getEmail());
//            }
//
//            // Set isFirstLogin based on the provided value or default it to true
//            boolean isFirstLogin = user.isFirstLogin();
//            if (!user.isFirstLogin()) {
//                isFirstLogin = true; // Defaulting isFirstLogin to true if not provided
//            }
//
//            // Set the isFirstLogin value in the user object
//            user.setFirstLogin(isFirstLogin);
//
//            // Save the user
//            userServices.saveUser(user);
//
//            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create user: " + e.getMessage());
//        }
//    }
//


    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody MD_Users user) {
        Map<String, Object> response = new HashMap<>();

        // Validate input: ensure at least one of first name or last name is provided
        if ((user.getFirstName() == null || user.getFirstName().trim().isEmpty()) &&
                (user.getLastName() == null || user.getLastName().trim().isEmpty())) {
            response.put("message", "At least one of first name or last name must be provided");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Check if a user with the same email already exists
        if (userServices.userExistsByEmail(user.getEmail())) {
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
            MD_Users createdUser = usersRepo.save(user);

            // Handle role assignments
            for (String role : user.getRole()) {
                switch (role) {
                    case "Delivery Director":
                        userService.createDeliveryDirector(user);
                        break;
                    case "Delivery Manager":
                        userService.createDeliveryManager(user);
                        break;
                    case "Project Manager":
                        userService.createProjectManager(user);
                        break;
                    // Handle other roles if necessary
                    default:
                        // Handle unknown roles or ignore
                        break;
                }
            }

            // Prepare the response with a success message and the created user
            response.put("message", "User with name " + createdUser.getFirstName() + " " +
                    createdUser.getLastName() + " and roles: " + createdUser.getRole() + " saved successfully.");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            response.put("message", "Failed to create user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }







    @GetMapping("/get")
    public ResponseEntity<List<MD_Users>> getAllUsers() {
        List<MD_Users> allUsers = usersRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }





//    @PutMapping("/update")
//    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Users updatedUser) {
//        // Validate input
//        if ((updatedUser.getFirstName() == null || updatedUser.getFirstName().trim().isEmpty()) &&
//                (updatedUser.getLastName() == null || updatedUser.getLastName().trim().isEmpty())) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Collections.singletonMap("message", "At least one of first name or last name must be provided"));
//        }
//        Optional<MD_Users> userOptional = usersRepo.findById(updatedUser.getId());
//
//        if (!userOptional.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Collections.singletonMap("message", "User not found"));
//        }
//        // Check for duplicates
//        List<MD_Users> duplicateUsers = usersRepo.findByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName());
//        for (MD_Users user : duplicateUsers) {
//            if (user.getId() !=(updatedUser.getId())) {
//                return ResponseEntity.status(HttpStatus.CONFLICT)
//                        .body(Collections.singletonMap("message", "Duplicate entry: User with first name '"
//                                + updatedUser.getFirstName() + "' and last name '" + updatedUser.getLastName() + "' already exists."));
//            }
//        }
//
//        MD_Users existingUser = userOptional.get();
//        StringBuilder updateMessage = new StringBuilder();
//        boolean isUpdated = false;
//
//        String oldFullName = existingUser.getFirstName() + " " + existingUser.getLastName();
//        String newFullName = updatedUser.getFirstName() + " " + updatedUser.getLastName();
//
//
//        // Update the user name if changed
//        if (!existingUser.getFirstName().equals(updatedUser.getFirstName()) ||
//                !existingUser.getLastName().equals(updatedUser.getLastName())) {
//            existingUser.setFirstName(updatedUser.getFirstName());
//            existingUser.setLastName(updatedUser.getLastName());
//            updateMessage.append("Name updated from '").append(oldFullName).append("' to '").append(newFullName).append("'. ");
//            isUpdated = true;
//        }
//
//        // Update roles first using the old full name
//        if (!existingUser.getRole().equals(updatedUser.getRole())) {
//
//            userService.updateRoles(existingUser, updatedUser);
//            existingUser.setRole(updatedUser.getRole());
//
//            updateMessage.append("user updated.");
//            isUpdated = true;
//        }
//
//        if (isUpdated) {
//            // Save the updated user
//            usersRepo.save(existingUser);
//            // Update role entities with the new name
//            userService.updateRoleEntities(existingUser, oldFullName, newFullName);
//
//        } else {
//            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
//        }
//
//
//
//
//
//        // Prepare response
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", updateMessage.toString());
//        response.put("updatedUser", existingUser);
//
//        return ResponseEntity.ok(response);
//    }




    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Users updatedUser) {
                // Validate input
        if ((updatedUser.getFirstName() == null || updatedUser.getFirstName().trim().isEmpty()) &&
                (updatedUser.getLastName() == null || updatedUser.getLastName().trim().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "At least one of first name or last name must be provided"));
        }
        Optional<MD_Users> userOptional = usersRepo.findById(updatedUser.getId());

        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("message", "User not found"));
        }

        // Check for duplicates
        List<MD_Users> duplicateUsers = usersRepo.findByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName());
        for (MD_Users user : duplicateUsers) {
            if (user.getId() != (updatedUser.getId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Collections.singletonMap("message", "Duplicate entry: User with first name '"
                                + updatedUser.getFirstName() + "' and last name '" + updatedUser.getLastName() + "' already exists."));
            }
        }

        MD_Users existingUser = userOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        String oldFullName = existingUser.getFirstName() + " " + existingUser.getLastName();
        String newFullName = updatedUser.getFirstName() + " " + updatedUser.getLastName();

        // Update roles first using the old full name
        if (!existingUser.getRole().equals(updatedUser.getRole())) {
            userService.updateRoles(existingUser, updatedUser);
            existingUser.setRole(updatedUser.getRole());
            isUpdated = true;
            updateMessage.append("Roles updated. ");
        }

        // Update the user name if changed
        if (!existingUser.getFirstName().equals(updatedUser.getFirstName()) ||
                !existingUser.getLastName().equals(updatedUser.getLastName())) {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            updateMessage.append("Name updated from '").append(oldFullName).append("' to '").append(newFullName).append("'. ");
            isUpdated = true;
        }

        if (isUpdated) {
            // Save the updated user
            usersRepo.save(existingUser);
            // Update role entities with the new name
            userService.updateRoleEntities(existingUser, oldFullName, newFullName);
        } else {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedUser", existingUser);

        return ResponseEntity.ok(response);
    }




    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteUsersByIds(@RequestBody List<Integer> ids) {
        List<Integer> notFoundIds = new ArrayList<>();
        List<String> deletedUserNames = new ArrayList<>();

        for (Integer id : ids) {
            Optional<MD_Users> userOptional = usersRepo.findById(id);
            if (userOptional.isEmpty()) {
                notFoundIds.add(id);
            } else {
                MD_Users user = userOptional.get();
                deletedUserNames.add(user.getFirstName() + " " + user.getLastName());
                userService.deleteRelatedEntities(user);
                usersRepo.deleteById(id);
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















    @PostMapping("/checkUserAndReturnResponse")
    public ResponseEntity<Map<String, Object>> checkUserAndReturnResponse(@RequestBody MD_Users user) {
        try {
            String email = user.getEmail();
            String password = user.getPassword();

            // Check if user with provided email exists in the database
            boolean emailExists = userServices.userExistsByEmail(email);
            if (!emailExists) {
                // User with the provided email does not exist
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("response", "Email provided is incorrect"));
            }
            // Get the existing user
            MD_Users existingUser = userServices.getUserByEmail(email);

            // Check if the provided password matches the one associated with the email
            boolean passwordCorrect = userServices.verifyPassword(email, password);
            //System.out.println(passwordCorrect);
            if (passwordCorrect) {
                // Check if isFirstLogin is true
                boolean isFirstLogin = existingUser.isFirstLogin();
                //System.out.println(isFirstLogin);
                if (isFirstLogin) {
                    // Update isFirstLogin to false and save the user
                    existingUser.setFirstLogin(false);
                    userServices.saveUser(existingUser);

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
            MD_Users existingUser = userServices.getUserByEmail(email);

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
            userServices.saveUser(existingUser);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(Collections.singletonMap("response", "Password updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("response", "Failed to update password: " + e.getMessage()));
        }
    }




    //doesn't check for old password tho, that's something I need help with to fix-->
    @PutMapping("/admin/{email}")
    public ResponseEntity<String> updateUser(@PathVariable("email") String email, @RequestBody MD_Users updatedUser) {
        try {
            // Retrieve the user by email
            MD_Users existingUser = userServices.getUserByEmail(email);
            if (existingUser == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }

            // Update the email if provided
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                existingUser.setEmail(updatedUser.getEmail());
            }

            // Update the password if provided and encrypt it
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                String encryptedPassword = AESUtil.encrypt(updatedUser.getPassword());
                existingUser.setPassword(encryptedPassword);
            }

            // Set isFirstLogin to false after updating the password
            existingUser.setFirstLogin(false);

            // Save the updated user
            userServices.saveUser(existingUser);

            return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user: " + e.getMessage());
        }
    }


}
