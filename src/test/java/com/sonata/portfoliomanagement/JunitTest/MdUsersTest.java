package com.sonata.portfoliomanagement.JunitTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.controllers.MD_UsersController;
import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.interfaces.MD_DeliveryManagerRepository;
import com.sonata.portfoliomanagement.interfaces.MD_ProjectManagerRepository;
import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import com.sonata.portfoliomanagement.model.MD_Users;
import com.sonata.portfoliomanagement.services.AESUtil;
import com.sonata.portfoliomanagement.services.Md_UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.*;
//import org.mockito.junit.MockitoJUnitRunner;
//
//@RunWith(MockitoJUnitRunner.class)
public class MdUsersTest{
    @InjectMocks
    private MD_UsersController usersController;

    //    @Mock
//    private Md_UserService userService;
    @Mock
    private MD_UsersRepository usersRepo;

    @Mock
    private MD_DeliveryDirectorRepository deliveryDirectorRepository;

    @Mock
    private MD_DeliveryManagerRepository deliveryManagerRepository;

    @Mock
    private MD_ProjectManagerRepository projectManagerRepository;

    @Mock
    private AESUtil aesUtil;
    @InjectMocks
    private Md_UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testGetAllUsers() {
//        // Mock data
//        MD_Users user1 = new MD_Users(1, "John", "Doe", Arrays.asList("ROLE_USER"), "john.doe@example.com", "password", false);
//        MD_Users user2 = new MD_Users(2, "Jane", "Smith", Arrays.asList("ROLE_ADMIN"), "jane.smith@example.com", "password123", true);
//        List<MD_Users> mockUsers = Arrays.asList(user1, user2);
//
//        // Mock repository method call
//        when(usersRepository.findAll()).thenReturn(mockUsers);
//
//        // Call the controller method
//        ResponseEntity<List<MD_Users>> response = usersController.getAllUsers();
//
//        // Assertions
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(2, response.getBody().size()); // Assuming you expect 2 users in the response
//        assertEquals("John", response.getBody().get(0).getFirstName());
//        assertEquals("Doe", response.getBody().get(0).getLastName());
//        assertEquals("ROLE_USER", response.getBody().get(0).getRole().get(0));
//        assertEquals("john.doe@example.com", response.getBody().get(0).getEmail());
//        assertEquals("Jane", response.getBody().get(1).getFirstName());
//        assertEquals("Smith", response.getBody().get(1).getLastName());
//        assertEquals("ROLE_ADMIN", response.getBody().get(1).getRole().get(0));
//        assertEquals("jane.smith@example.com", response.getBody().get(1).getEmail());
//    }

//    @Test
//    public void testGetAllUsers() {
//        // Prepare some mock data
//        List<MD_Users> mockUsers = Arrays.asList(
//                new MD_Users(1, "John", "Doe", Arrays.asList("ROLE_USER"), "john.doe@example.com", "password", false),
//                new MD_Users(2, "Jane", "Smith", Arrays.asList("ROLE_ADMIN"), "jane.smith@example.com", "password", false)
//        );
//
//        // Mock the behavior of the repository
//        when(usersRepo.findAll()).thenReturn(mockUsers);
//
//        // Call the controller method
//        ResponseEntity<List<MD_Users>> responseEntity = usersController.getAllUsers();
//
//        // Verify the response
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        List<MD_Users> returnedUsers = responseEntity.getBody();
//        assertEquals(mockUsers.size(), returnedUsers.size());
//        assertEquals(mockUsers.get(0).getFirstName(), returnedUsers.get(0).getFirstName());
//        assertEquals(mockUsers.get(1).getFirstName(), returnedUsers.get(1).getFirstName());
//
//        // Add more assertions as needed for other fields
//
//        // Optionally, verify repository method invocation
//        // verify(usersRepo, times(1)).findAll();
//    }
    @Test
    public void testCreateUserWithRole() {
        // Prepare mock user
        MD_Users mockUser = new MD_Users();
        mockUser.setId(1);
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john.doe@example.com");
        mockUser.setRole(List.of("Delivery Director"));
        mockUser.setFirstLogin(true);

        // Mock repository behavior
        when(usersRepo.save(any(MD_Users.class))).thenReturn(mockUser);

        // Call the service method
        MD_Users createdUser = userService.createUserWithRole(mockUser);

        // Verify the saved user object
        assertEquals(1, createdUser.getId());
        assertEquals("John", createdUser.getFirstName());
        assertEquals("Doe", createdUser.getLastName());

        // Verify repository save method invocation
        verify(usersRepo, times(1)).save(any(MD_Users.class));

        // Verify role-specific method invocation
        verify(deliveryDirectorRepository, times(1)).save(any(MD_DeliveryDirector.class));
        verify(deliveryManagerRepository, never()).save(any(MD_DeliveryManager.class));
        verify(projectManagerRepository, never()).save(any(MD_ProjectManager.class));
    }

    @Test
    public void testCreateDeliveryDirector() {
        // Prepare mock user
        MD_Users mockUser = new MD_Users();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        // Call the service method
        userService.createDeliveryDirector(mockUser);

        // Verify repository save method invocation
        verify(deliveryDirectorRepository, times(1)).save(any(MD_DeliveryDirector.class));
    }

//    @Test
//    public void testUpdateRoles() {
//        // Prepare mock users
//        MD_Users existingUser = new MD_Users();
//        existingUser.setId(1);
//        existingUser.setFirstName("John");
//        existingUser.setLastName("Doe");
//        existingUser.setRole(List.of("Delivery Director"));
//
//        MD_Users updatedUser = new MD_Users();
//        updatedUser.setId(1);
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setRole(List.of("Delivery Director", "Delivery Manager"));
//
//        // Mock repository find and delete methods
//        when(deliveryDirectorRepository.findByDeliveryDirector("John Doe")).thenReturn(new ArrayList<>());
//        doNothing().when(deliveryDirectorRepository).deleteByDeliveryDirector("John Doe");
//
//        // Call the service method
//        userService.updateRoles(existingUser, updatedUser);
//
//        // Verify role-specific method invocations
//        verify(deliveryDirectorRepository, times(1)).findByDeliveryDirector("John Doe");
//        verify(deliveryDirectorRepository, times(1)).deleteByDeliveryDirector("John Doe");
//        verify(deliveryManagerRepository, times(1)).save(any(MD_DeliveryManager.class));
//    }
//    @Test
//    public void testUpdateRoles() {
//        // Prepare mock users
//        MD_Users existingUser = new MD_Users();
//        existingUser.setId(1);
//        existingUser.setFirstName("John");
//        existingUser.setLastName("Doe");
//        existingUser.setRole(List.of("Delivery Director"));
//
//        MD_Users updatedUser = new MD_Users();
//        updatedUser.setId(1);
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setRole(List.of("Delivery Director", "Delivery Manager"));
//
//        // Mock repository behavior for deliveryDirectorRepository
//        when(deliveryDirectorRepository.findByDeliveryDirector("John Doe")).thenReturn(new ArrayList<>());
//
//        // Call the service method
//        userService.updateRoles(existingUser, updatedUser);
//
//        // Verify repository method invocation
//        verify(deliveryDirectorRepository, times(1)).findByDeliveryDirector("John Doe");
//        verify(deliveryDirectorRepository, times(1)).deleteByDeliveryDirector("John Doe");
//        verify(deliveryManagerRepository, times(1)).save(any(MD_DeliveryManager.class));
//    }

//    @Test
//    void testCreateUserWithRole_Success() {
//        // Mock user data
//        MD_Users user = new MD_Users();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setRole(Collections.singletonList("Delivery Director"));
//
//        // Mock repository save method
//        when(usersRepo.save(user)).thenReturn(user);
//
//        // Call service method
//        MD_Users createdUser = userService.createUserWithRole(user);
//
//        // Verify repository method invocation
//        verify(usersRepo, times(1)).save(user);
//
//        // Assert the created user
//        assertEquals("John", createdUser.getFirstName());
//        assertEquals("Doe", createdUser.getLastName());
//        assertEquals(1, createdUser.getRole().size());
//        assertEquals("Delivery Director", createdUser.getRole().get(0));
//    }

    //    @Test
//    void testUpdateRoles_Success() {
//        // Mock existing user
//        MD_Users existingUser = new MD_Users();
//        existingUser.setFirstName("John");
//        existingUser.setLastName("Doe");
//        existingUser.setRole(Collections.singletonList("Delivery Director"));
//
//        // Mock updated user
//        MD_Users updatedUser = new MD_Users();
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setRole(Collections.singletonList("Delivery Manager"));
//
//        // Mock repository methods
//        when(deliveryDirectorRepository.findByDeliveryDirector("John Doe")).thenReturn(Collections.emptyList());
//
//        // Call service method
//        userService.updateRoles(existingUser, updatedUser);
//
//        // Verify repository method invocations
//        verify(deliveryDirectorRepository, times(1)).findByDeliveryDirector("John Doe");
//        verify(deliveryDirectorRepository, times(1)).deleteByDeliveryDirector("John Doe");
//        verify(deliveryManagerRepository, times(1)).save(any());
//    }
//    @Test
//    public void testUpdateRoles_Success() {
//        // Setup
//        MD_Users existingUser = new MD_Users();
//        existingUser.setFirstName("John");
//        existingUser.setLastName("Doe");
//        existingUser.setEmail("john.doe@example.com");
//        existingUser.setRole(Collections.singletonList("Delivery Director"));
//
//        MD_Users updatedUser = new MD_Users();
//        updatedUser.setFirstName("John");
//        updatedUser.setLastName("Doe");
//        updatedUser.setEmail("john.doe@example.com");
//        updatedUser.setRole(Collections.singletonList("Project Manager"));
//
//        // Mocking repository interactions
//        Mockito.when(usersRepo.save(Mockito.any(MD_Users.class))).thenReturn(existingUser);
//
//        // Invoke the method under test
//        userService.updateRoles(existingUser, updatedUser);
//
//        // Verify interactions
//        Mockito.verify(deliveryDirectorRepository, Mockito.times(1)).findByDeliveryDirector(Mockito.eq("John Doe"));
//        Mockito.verify(deliveryDirectorRepository, Mockito.times(1)).deleteByDeliveryDirector(Mockito.eq("John Doe"));
//    }
//    @Test
//    public void testDeleteUsersByIds_Success() throws Exception {
//        // Mock data
//        MD_Users user1 = new MD_Users();
//        user1.setId(1);
//        user1.setFirstName("John");
//        user1.setLastName("Doe");
//
//        MD_Users user2 = new MD_Users();
//        user2.setId(2);
//        user2.setFirstName("Jane");
//        user2.setLastName("Smith");
//
//        List<Integer> idsToDelete = Arrays.asList(1, 2);
//        List<String> deletedUserNames = Arrays.asList("John Doe", "Jane Smith");
//
//        // Mock repository behavior
//        when(usersRepo.findById(1)).thenReturn(Optional.of(user1));
//        when(usersRepo.findById(2)).thenReturn(Optional.of(user2));
//
//        // Mock service behavior
//        // Assuming userService.deleteRelatedEntities() does not throw exceptions and behaves correctly
//
//        // Perform DELETE request to controller
//        ResponseEntity<Map<String, Object>> responseEntity = usersController.deleteUsersByIds(idsToDelete);
//
//        // Verify response
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals("Users: John Doe, Jane Smith deleted successfully", responseEntity.getBody().get("message"));
//
//        // Verify interactions
//        verify(usersRepo, times(1)).findById(1);
//        verify(usersRepo, times(1)).findById(2);
//        verify(userService, times(1)).deleteRelatedEntities(user1);
//        verify(userService, times(1)).deleteRelatedEntities(user2);
//        verify(usersRepo, times(1)).deleteById(1);
//        verify(usersRepo, times(1)).deleteById(2);
//    }
//
//    @Test
//    public void testDeleteUsersByIds_UserNotFound() throws Exception {
//        // Mock data
//        List<Integer> idsToDelete = Arrays.asList(1, 2);
//        List<Integer> notFoundIds = Arrays.asList(2); // Simulate user with id 2 not found
//
//        // Mock repository behavior
//        when(usersRepo.findById(1)).thenReturn(Optional.of(new MD_Users())); // Return empty user for id 1
//        when(usersRepo.findById(2)).thenReturn(Optional.empty()); // Return empty Optional for id 2
//
//        // Perform DELETE request to controller
//        ResponseEntity<Map<String, Object>> responseEntity = usersController.deleteUsersByIds(idsToDelete);
//
//        // Verify response
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//        assertEquals("No users found with IDs: [2]", responseEntity.getBody().get("message"));
//
//        // Verify interactions
//        verify(usersRepo, times(1)).findById(1);
//        verify(usersRepo, times(1)).findById(2);
//        verify(usersRepo, never()).deleteById(any()); // Ensure deleteById is never called for not found user
//        verify(userService, never()).deleteRelatedEntities(any()); // Ensure deleteRelatedEntities is never called
//    }
//







}





