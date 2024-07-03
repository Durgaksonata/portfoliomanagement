package com.sonata.portfoliomanagement.JunitTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.controllers.MD_RolesController;
import com.sonata.portfoliomanagement.interfaces.MD_RolesRepository;
import com.sonata.portfoliomanagement.model.MD_Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@WebMvcTest(MD_RolesController.class)
public class MD_RolesTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MD_RolesRepository roleRepo;

    private MD_Role role1;
    private MD_Role role2;

    @BeforeEach
    void setUp() {
        role1 = new MD_Role(1, "Admin");
        role2 = new MD_Role(2, "User");
    }

    // Define a helper method to convert objects to JSON string
    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllRoles() throws Exception {
        when(roleRepo.findAll()).thenReturn(Arrays.asList(role1, role2));

        mockMvc.perform(get("/mdrole/get"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].role", is(role1.getRole())))
                .andExpect(jsonPath("$[1].role", is(role2.getRole())));
    }

    @Test
    void testCreateRole_Success() throws Exception {
        when(roleRepo.findByRole(role1.getRole())).thenReturn(Collections.emptyList());
        when(roleRepo.save(any(MD_Role.class))).thenReturn(role1);

        mockMvc.perform(post("/mdrole/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(role1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", containsString("created successfully.")))
                .andExpect(jsonPath("$.createdRole.role", is(role1.getRole())));
    }

//	    @Test
//	    void testCreateRole_Duplicate() throws Exception {
//	        when(roleRepo.findByRole(role1.getRole())).thenReturn(Collections.singletonList(role1));
//
//	        mockMvc.perform(post("/mdrole/save")
//	                .contentType(MediaType.APPLICATION_JSON)
//	                .content(asJsonString(role1)))
//	                .andExpect(status().isConflict())
//	                .andExpect(jsonPath("$.message", containsString("Duplicate Entry")));
//	    }

    @Test
    void testUpdateRole_Success() throws Exception {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.of(role1));
        when(roleRepo.findByRole(role1.getRole())).thenReturn(Collections.emptyList());
        when(roleRepo.save(any(MD_Role.class))).thenReturn(role1);

        MD_Role updatedRole = new MD_Role(1, "SuperAdmin");

        mockMvc.perform(put("/mdrole/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedRole)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("updated")))
                .andExpect(jsonPath("$.updatedRole.role", is(updatedRole.getRole())));
    }


    @Test
    void testUpdateRole_NotFound() throws Exception {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/mdrole/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(role1)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("not found")));
    }

    @Test
    void testDeleteRolesByIds_Success() throws Exception {
        when(roleRepo.findById(role1.getId())).thenReturn(Optional.of(role1));

        mockMvc.perform(delete("/mdrole/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Collections.singletonList(role1.getId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("deleted successfully")))
                .andExpect(jsonPath("$.deletedRoles", hasSize(1)))
                .andExpect(jsonPath("$.deletedRoles[0]", is(role1.getRole())));
    }

    @Test
    void testDeleteRolesByIds_NotFound() throws Exception {
        when(roleRepo.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/mdrole/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(Collections.singletonList(role1.getId()))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("Invalid Entries")));
    }






}
