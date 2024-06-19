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
    @Autowired
    private Md_UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Object> createUser(@RequestBody MD_Users user) {
        List<MD_Users> existingUsers = usersRepo.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        if (!existingUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Duplicate entries: Users with first name '" + user.getFirstName() +
                            "' and last name '" + user.getLastName() + "' already exist.");
        }

        // Save the new user if no conflicts are found
        MD_Users createdUser = usersRepo.save(user);

        // Handle role assignments
        for (String role : user.getRole()) {
            if (role.equals("DeliveryDirector")) {
                userService.createDeliveryDirector(user);
            } else if (role.equals("DeliveryManager")) {
                userService.createDeliveryManager(user);
            } else if (role.equals("ProjectManager")) {
                userService.createProjectManager(user);
            } else {
                // Handle other roles if necessary
            }
        }

        // Prepare the response with a success message and the created user
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User with name " + createdUser.getFirstName() + " " + createdUser.getLastName() + " saved successfully.");
      //  response.put("createdUser", createdUser);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }



    @GetMapping("/get")
    public ResponseEntity<List<MD_Users>> getAllUsers() {
        List<MD_Users> allUsers = usersRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/update")
    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Users updatedUser) {
        Optional<MD_Users> userOptional = usersRepo.findById(updatedUser.getId());

        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        List<MD_Users> duplicateUsers = usersRepo.findByFirstNameAndLastName(updatedUser.getFirstName(), updatedUser.getLastName());
        if (!duplicateUsers.isEmpty()) {
            for (MD_Users user : duplicateUsers) {
                if (user.getId() != updatedUser.getId()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("message", "Duplicate entry: User with first name '" + updatedUser.getFirstName()
                                    + "' and last name '" + updatedUser.getLastName() + "' already exists."));
                }
            }
        }

        MD_Users existingUser = userOptional.get();
        StringBuilder updateMessage = new StringBuilder();
        boolean isUpdated = false;

        if (!existingUser.getFirstName().equals(updatedUser.getFirstName())) {
            updateMessage.append("First name updated from '")
                    .append(existingUser.getFirstName())
                    .append("' to '")
                    .append(updatedUser.getFirstName())
                    .append("'. ");
            existingUser.setFirstName(updatedUser.getFirstName());
            isUpdated = true;
        }

        if (!existingUser.getLastName().equals(updatedUser.getLastName())) {
            updateMessage.append("Last name changed from '")
                    .append(existingUser.getLastName())
                    .append("' to '")
                    .append(updatedUser.getLastName())
                    .append("'. ");
            existingUser.setLastName(updatedUser.getLastName());
            isUpdated = true;
        }

        // Check for role updates and handle accordingly
        if (!existingUser.getRole().equals(updatedUser.getRole())) {
            updateMessage.append("Roles updated. ");
            userService.updateRoles(existingUser, updatedUser);
            existingUser.setRole(updatedUser.getRole());
            isUpdated = true;
        }

        if (!isUpdated) {
            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
        }

        MD_Users updatedUserEntity = usersRepo.save(existingUser);
        Map<String, Object> response = new HashMap<>();
        response.put("message", updateMessage.toString());
        response.put("updatedUser", updatedUserEntity);

        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUsersByIds(@RequestBody List<Integer> ids) {
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

        if (!notFoundIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No users found with IDs: " + notFoundIds.toString());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Users: " + deletedUserNames + " deleted successfully");
    }



}