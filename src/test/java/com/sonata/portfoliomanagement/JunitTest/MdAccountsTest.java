package com.sonata.portfoliomanagement.JunitTest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sonata.portfoliomanagement.controllers.MD_AccountsController;
import com.sonata.portfoliomanagement.interfaces.MD_AccountsRepository;
import com.sonata.portfoliomanagement.model.MD_Accounts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class MdAccountsTest {

    @InjectMocks
    private MD_AccountsController accountsController;

    @Mock
    private MD_AccountsRepository accountsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAccounts() {
        // Mock data
        MD_Accounts account1 = new MD_Accounts(1, "Account1");
        MD_Accounts account2 = new MD_Accounts(2, "Account2");
        List<MD_Accounts> mockAccounts = Arrays.asList(account1, account2);

        // Mock repository method call
        when(accountsRepository.findAll()).thenReturn(mockAccounts);

        // Call the controller method
        ResponseEntity<List<MD_Accounts>> response = accountsController.getAllAccounts();

        // Assertions
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size()); // Assuming you expect 2 accounts in the response
        assertEquals("Account1", response.getBody().get(0).getAccounts());
        assertEquals("Account2", response.getBody().get(1).getAccounts());
    }


    @Test
    public void testCreateAccount_Success() {
        // Assigning a specific ID value for testing
        Integer accountId = 123; // Replace with any integer ID you want to assign

        // Create a new account with the assigned ID
        MD_Accounts newAccount = new MD_Accounts(accountId, "NewAccount");

        // Mock repository method to return the new account when saved
        when(accountsRepository.save(newAccount)).thenReturn(newAccount);

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = accountsController.createAccount(newAccount);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("message"));
        assertTrue(responseBody.containsKey("createdAccount"));

        String message = (String) responseBody.get("message");
        assertEquals("Account created successfully with name 'NewAccount'.", message);

        MD_Accounts createdAccount = (MD_Accounts) responseBody.get("createdAccount");
        assertNotNull(createdAccount);
        assertEquals(accountId, createdAccount.getId());
        assertEquals("NewAccount", createdAccount.getAccounts());
    }
    @Test
    public void testCreateAccount_Conflict() {
        // Mock existing account with the same name
        MD_Accounts existingAccount = new MD_Accounts(1, "ExistingAccount");

        // Mock new account to be saved
        MD_Accounts newAccount = new MD_Accounts(2, "ExistingAccount");

        // Mock repository method call to return list with existing account
        when(accountsRepository.findByAccounts(newAccount.getAccounts())).thenReturn(Collections.singletonList(existingAccount));

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = accountsController.createAccount(newAccount);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertEquals("Account with name 'ExistingAccount' already exists.", responseBody.get("message"));
    }

    @Test
    public void testUpdateAccount_Success() {
        // Existing account in the mock repository
        int existingAccountId = 1;
        String existingAccountName = "ExistingAccount";
        MD_Accounts existingAccount = new MD_Accounts(existingAccountId, existingAccountName);

        // Mock repository behavior
        when(accountsRepository.findById(existingAccountId)).thenReturn(Optional.of(existingAccount));
        when(accountsRepository.findByAccounts(existingAccountName)).thenReturn(Collections.emptyList());
        when(accountsRepository.save(existingAccount)).thenReturn(existingAccount);

        // Updated account details
        String updatedAccountName = "UpdatedAccount";
        MD_Accounts updatedAccount = new MD_Accounts(existingAccountId, updatedAccountName);

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = accountsController.updateAccount(updatedAccount);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("message"));
        assertTrue(responseBody.containsKey("updatedAccount"));

        String message = (String) responseBody.get("message");
        MD_Accounts returnedAccount = (MD_Accounts) responseBody.get("updatedAccount");
        assertEquals("Account name updated from '" + existingAccountName + "' to '" + updatedAccountName + "'. ", message);
        assertEquals(updatedAccountName, returnedAccount.getAccounts());

        verify(accountsRepository).findById(existingAccountId);
        verify(accountsRepository).findByAccounts(updatedAccountName);
        verify(accountsRepository).save(existingAccount);
    }

    @Test
    public void testUpdateAccount_NotFound() {
        // Non-existent account ID
        int nonExistentAccountId = 999;
        MD_Accounts updatedAccount = new MD_Accounts(nonExistentAccountId, "UpdatedAccount");

        // Mock repository behavior for not finding the account
        when(accountsRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        // Call the controller method
        ResponseEntity<Map<String, Object>> responseEntity = accountsController.updateAccount(updatedAccount);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        Map<String, Object> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("message"));

        String message = (String) responseBody.get("message");
        assertEquals("Account with ID '" + nonExistentAccountId + "' not found.", message);

        verify(accountsRepository).findById(nonExistentAccountId);
        // Ensure no other interactions with repository
    }

//    @Test
//    public void testUpdateAccount_Conflict() {
//        // Existing account in the mock repository
//        int existingAccountId = 1;
//        String existingAccountName = "ExistingAccount";
//        MD_Accounts existingAccount = new MD_Accounts(existingAccountId, existingAccountName);
//
//        // Mock repository behavior
//        when(accountsRepository.findById(existingAccountId)).thenReturn(Optional.of(existingAccount));
//        when(accountsRepository.findByAccounts(existingAccountName)).thenReturn(List.of(existingAccount));
//
//        // Attempting to update with conflicting account name
//        String conflictingAccountName = "AnotherAccount";
//        MD_Accounts conflictingAccount = new MD_Accounts(existingAccountId, conflictingAccountName);
//
//        // Call the controller method
//        ResponseEntity<Map<String, Object>> responseEntity = accountsController.updateAccount(conflictingAccount);
//
//        // Assertions
//        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//        Map<String, Object> responseBody = responseEntity.getBody();
//        assertNotNull(responseBody);
//        assertTrue(responseBody.containsKey("message"));
//
//        String message = (String) responseBody.get("message");
//        assertEquals("Account with name '" + conflictingAccountName + "' already exists.", message);
//
//        verify(accountsRepository).findById(existingAccountId);
//        verify(accountsRepository).findByAccounts(conflictingAccountName);
//        // Ensure no save operation called due to conflict
//    }

    //    @Test
//    public void testUpdateAccountConflict() {
//        // Existing account in the mock repository
//        int existingAccountId = 1;
//        String existingAccountName = "ExistingAccount";
//        MD_Accounts existingAccount = new MD_Accounts(existingAccountId, existingAccountName);
//
//        // Mock repository behavior
//        when(accountsRepository.findById(existingAccountId)).thenReturn(Optional.of(existingAccount));
//        when(accountsRepository.findByAccounts(existingAccountName)).thenReturn(List.of(existingAccount));
//
//        // Attempting to update with conflicting account name
//        String conflictingAccountName = "AnotherAccount";
//        MD_Accounts conflictingAccount = new MD_Accounts(existingAccountId, conflictingAccountName);
//
//        // Call the controller method
//        ResponseEntity<Map<String, Object>> responseEntity = accountsController.updateAccount(conflictingAccount);
//
//        // Assertions
//        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//        Map<String, Object> responseBody = responseEntity.getBody();
//        assertNotNull(responseBody);
//        assertTrue(responseBody.containsKey("message"));
//
//        String message = (String) responseBody.get("message");
//        assertEquals("Account with name '" + conflictingAccountName + "' already exists.", message);
//
//        verify(accountsRepository).findById(existingAccountId);
//        verify(accountsRepository).findByAccounts(conflictingAccountName);
//        // Ensure no save operation called due to conflict
//    }
//
    @Test
    public void testDeleteAccountsByIds_Success() {
        // Mock data
        Integer id1 = 1;
        Integer id2 = 2;
        MD_Accounts account1 = new MD_Accounts(id1, "Account1");
        MD_Accounts account2 = new MD_Accounts(id2, "Account2");

        // Mock repository behavior
        when(accountsRepository.findById(id1)).thenReturn(Optional.of(account1));
        when(accountsRepository.findById(id2)).thenReturn(Optional.of(account2));
        doNothing().when(accountsRepository).deleteById(any());

        // Call the controller method
        List<Integer> idsToDelete = Arrays.asList(id1, id2);
        ResponseEntity<String> response = accountsController.deleteAccountsByIds(idsToDelete);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Accounts: [Account1, Account2] deleted successfully.", response.getBody());
    }

    @Test
    public void testDeleteAccountsByIds_NotFound() {
        // Mock data
        Integer id1 = 1;
        Integer id2 = 2;

        // Mock repository behavior
        when(accountsRepository.findById(id1)).thenReturn(Optional.empty());
        when(accountsRepository.findById(id2)).thenReturn(Optional.empty());

        // Call the controller method
        List<Integer> idsToDelete = Arrays.asList(id1, id2);
        ResponseEntity<String> response = accountsController.deleteAccountsByIds(idsToDelete);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No accounts found with IDs: [1, 2]", response.getBody());
    }

}



