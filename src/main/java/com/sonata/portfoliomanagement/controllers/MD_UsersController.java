package com.sonata.portfoliomanagement.controllers;

import java.util.*;

import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;
import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_Accounts;
import com.sonata.portfoliomanagement.model.MD_Users;
import com.sonata.portfoliomanagement.services.Md_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("/users")
public class MD_UsersController {

    @Autowired
    private MD_UsersRepository usersRepo;

    private Md_UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Object> createUser(@RequestBody MD_Users user) {
        List<MD_Users> existingUsers = usersRepo.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        if (!existingUsers.isEmpty()) {
            // Handle the case where multiple users with the same first name and last name exist
            // For example, return a conflict response
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entries: Users with first name '" + user.getFirstName() +
                            "' and last name '" + user.getLastName() + "' already exist.");
        }

        // Save the new user if no conflicts are found
        MD_Users createdUser = usersRepo.save(user);

        //switch (user.getRole()) {
        for (String role : user.getRole()) {
            switch (role) {
                case "DeliveryDirector":
                    userService.createDeliveryDirector(user);
                    break;
                case "DeliveryManager":
                    userService.createDeliveryManager(user);
                    break;
                case "ProjectManager":
                    userService.createProjectManager(user);
                    break;
                // Add more cases as needed for other roles
                default:
                    break;
            }
        }

        // Prepare the response with a success message and the created user
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User with name " + createdUser.getFirstName() + " " + createdUser.getLastName() + " saved successfully.");
        response.put("createdUser", createdUser);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }


    @GetMapping("/get")
    public ResponseEntity<List<MD_Users>> getAllUsers() {
        List<MD_Users> allUsers = usersRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Users updatedUser) {
        // Check if the user with the given ID exists
        Optional<MD_Users> userOptional = usersRepo.findById(updatedUser.getId());

        if (!userOptional.isPresent()) {
            // If user with the given ID is not found, return 404 Not Found
            return ResponseEntity.notFound().build();
        }

        List<MD_Users> duplicateUsers = usersRepo.findByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName());
        if (!duplicateUsers.isEmpty()) {
            for (MD_Users user : duplicateUsers) {
                //if (user.getId() != null && user.getId() != updatedUser.getId()) {
                if (user.getId() != updatedUser.getId()) {
                    // If a duplicate user exists and it's not the current user, return a conflict response
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("message", "Duplicate entry: User with first name '" + updatedUser.getFirstName()
                                    + "' and last name '" + updatedUser.getLastName() + "' already exists."));
                }
            }
        }

        // Update the existing user with the new values
        MD_Users existingUser = userOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;


        // Example: Check and update the first name
        if (!existingUser.getFirstName().equals(updatedUser.getFirstName())) {
            updateMessage.append("First name updated from '")
                    .append(existingUser.getFirstName())
                    .append("' to '")
                    .append(updatedUser.getFirstName())
                    .append("'. ");
            existingUser.setFirstName(updatedUser.getFirstName());
            isUpdated = true;

        }

        // Example: Check and update the last name
        if (!existingUser.getLastName().equals(updatedUser.getLastName())) {
            updateMessage.append("Last name changed from '")
                    .append(existingUser.getLastName())
                    .append("' to '")
                    .append(updatedUser.getLastName())
                    .append("'. ");
            existingUser.setLastName(updatedUser.getLastName());
            isUpdated = true;

        }
        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }

        // Example: Check and update the role or other fields as needed

        // Save the updated user
        MD_Users updatedUserEntity = usersRepo.save(existingUser);

        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedUser", updatedUserEntity);

        // Return the updated user with 200 OK status
        return ResponseEntity.ok(response);

    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<String> deleteUsersByIds(@RequestBody List<Integer> ids) {
//        List<Integer> notFoundIds = new ArrayList<>();
//        List<String> deletedUserNames = new ArrayList<>();
//
//        for (Integer id : ids) {
//            Optional<MD_Users> userOptional = usersRepo.findById(id);
//            if (userOptional.isEmpty()) {
//                notFoundIds.add(id);
//            } else {
//                MD_Users user = userOptional.get();
//                deletedUserNames.add(user.getFirstName() + " " + user.getLastName());
//                usersRepo.deleteById(id);
//            }
//        }
//
//        if (!notFoundIds.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("No users found with IDs: " + notFoundIds.toString());
//        }
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body("Users: " + deletedUserNames + " deleted successfully");
//    }


}