package com.sonata.portfoliomanagement.controllers;

import java.util.*;

import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_Users;
import com.sonata.portfoliomanagement.services.Md_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class MD_UsersController {

    @Autowired
    private MD_UsersRepository usersRepo;
    @Autowired
    private Md_UserService userService;

    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody MD_Users user) {
        // Validate input
        if ((user.getFirstName() == null || user.getFirstName().trim().isEmpty()) &&
                (user.getLastName() == null || user.getLastName().trim().isEmpty())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("message", "At least one of first name or last name must be provided"));
        }
        List<MD_Users> existingUsers = usersRepo.findByFirstNameAndLastName(user.getFirstName(), user.getLastName());

        if (!existingUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Collections.singletonMap("message", "Duplicate entries: Users with first name '"
                            + user.getFirstName() + "' and last name '" + user.getLastName() + "' already exist."));
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
            }
            // Handle other roles if necessary
        }

        // Prepare the response with a success message and the created user
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User with name " + createdUser.getFirstName() + " " + createdUser.getLastName() +
                " and roles: " + createdUser.getRole() + " saved successfully.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<MD_Users>> getAllUsers() {
        List<MD_Users> allUsers = usersRepo.findAll();
        return ResponseEntity.ok(allUsers);
    }


//
//    @PutMapping("/update")
//    public ResponseEntity<Map<String, Object>> updateMdUser(@RequestBody MD_Users updatedUser) {
//        Optional<MD_Users> userOptional = usersRepo.findById(updatedUser.getId());
//
//        if (!userOptional.isPresent()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(Collections.singletonMap("message", "User not found"));
//        }
//
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
//        boolean isFirstNameUpdated = false;
//        boolean isLastNameUpdated = false;
//        boolean areRolesUpdated = false;
//
//        String oldFirstName = existingUser.getFirstName();
//        String oldLastName = existingUser.getLastName();
//        List<String> oldRoles = new ArrayList<>(existingUser.getRole());
//
//        // Check for first name update
//        if (!existingUser.getFirstName().equals(updatedUser.getFirstName())) {
//            updateMessage.append("First name updated from '")
//                    .append(existingUser.getFirstName())
//                    .append("' to '")
//                    .append(updatedUser.getFirstName())
//                    .append("'. ");
//            existingUser.setFirstName(updatedUser.getFirstName());
//            isFirstNameUpdated = true;
//        }
//
//        // Check for last name update
//        if (!existingUser.getLastName().equals(updatedUser.getLastName())) {
//            updateMessage.append("Last name changed from '")
//                    .append(existingUser.getLastName())
//                    .append("' to '")
//                    .append(updatedUser.getLastName())
//                    .append("'. ");
//            existingUser.setLastName(updatedUser.getLastName());
//            isLastNameUpdated = true;
//        }
//
//        // Save name updates first
//        if (isFirstNameUpdated || isLastNameUpdated) {
//            existingUser = usersRepo.save(existingUser);
//        }
//
//        // Check for role updates
//        if (!oldRoles.containsAll(updatedUser.getRole()) || !updatedUser.getRole().containsAll(oldRoles)) {
//            updateMessage.append("Roles updated. ");
//            userService.updateRoles(existingUser, updatedUser);
//            existingUser.setRole(updatedUser.getRole());
//            areRolesUpdated = true;
//        }
//
//        // Save role updates
//        if (areRolesUpdated) {
//            userService.updateRoles(existingUser, updatedUser);
//            existingUser.setRole(updatedUser.getRole());
//            existingUser = usersRepo.save(existingUser);
//        }
//
//        // If no updates detected, return "No changes detected" message
//        if (!isFirstNameUpdated && !isLastNameUpdated && !areRolesUpdated) {
//            return ResponseEntity.ok(Collections.singletonMap("message", "No changes detected"));
//        }
//
//        // If the name was updated, update the role entities with the new name
//        if (isFirstNameUpdated || isLastNameUpdated) {
//            String oldFullName = oldFirstName + " " + oldLastName;
//            String newFullName = updatedUser.getFirstName() + " " + updatedUser.getLastName();
//            userService.updateRoleEntities(existingUser, oldFullName, newFullName);
//        }
//
//        // Prepare response
//        Map<String, Object> response = new HashMap<>();
//        response.put("message", updateMessage.toString());
//        response.put("updatedUser", existingUser);
//
//        return ResponseEntity.ok(response);
//    }
//


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
            if (user.getId() !=(updatedUser.getId())) {
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



        // Update the user name if changed
        if (!existingUser.getFirstName().equals(updatedUser.getFirstName()) ||
                !existingUser.getLastName().equals(updatedUser.getLastName())) {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            updateMessage.append("Name updated from '").append(oldFullName).append("' to '").append(newFullName).append("'. ");
            isUpdated = true;
        }


        // Update roles first using the old full name
        if (!existingUser.getRole().equals(updatedUser.getRole())) {

            userService.updateRoles(existingUser, updatedUser);
            existingUser.setRole(updatedUser.getRole());

            updateMessage.append("user updated.");
          //  isUpdated = true;
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
}
