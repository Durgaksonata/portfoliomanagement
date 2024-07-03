package com.sonata.portfoliomanagement.JunitTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.controllers.MD_VerticalController;
import com.sonata.portfoliomanagement.interfaces.MD_VerticalRepository;
import com.sonata.portfoliomanagement.model.MD_Vertical;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(MD_VerticalController.class)
public class MD_VerticalTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MD_VerticalRepository verticalRepo;

    // Helper method to convert objects to JSON string
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllData_Success() throws Exception {
        List<MD_Vertical> verticals = Arrays.asList(
                new MD_Vertical(1, "Vertical 1"),
                new MD_Vertical(2, "Vertical 2")
        );

        when(verticalRepo.findAll()).thenReturn(verticals);

        mockMvc.perform(get("/mdvertical/getall"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].vertical").value("Vertical 1"))
                .andExpect(jsonPath("$[1].vertical").value("Vertical 2"));

        verify(verticalRepo, times(1)).findAll();
    }



//    @Test
//    void testCreateMdVertical_Success() throws Exception {
//        // Mock data
//        MD_Vertical newVertical = new MD_Vertical(null, "Vertical 3");
//        MD_Vertical savedVertical = new MD_Vertical(3, "Vertical 3");
//
//        // Mock repository behavior
//        when(verticalRepo.findByVertical("Vertical 3")).thenReturn(Optional.empty()); // Mocking the case where no vertical with name "Vertical 3" exists
//        when(verticalRepo.save(any(MD_Vertical.class))).thenReturn(savedVertical); // Mocking the save operation
//
//        // Perform the POST request using mockMvc
//        mockMvc.perform(post("/mdvertical/save")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(newVertical))) // Convert newVertical to JSON string
//                .andExpect(status().isCreated()) // Expect HTTP status 201 Created
//                .andExpect(content().string("Data added: Vertical 'Vertical 3' added successfully.")); // Expect specific response content
//
//        // Verify repository interactions
//        verify(verticalRepo, times(1)).findByVertical("Vertical 3"); // Verify findByVertical was called once with "Vertical 3"
//        verify(verticalRepo, times(1)).save(any(MD_Vertical.class)); // Verify save was called once with any MD_Vertical object
//    }




    @Test
    void testUpdateMdVertical_Success() throws Exception {
        MD_Vertical existingVertical = new MD_Vertical(1, "Old Vertical");
        MD_Vertical updatedVertical = new MD_Vertical(1, "New Vertical");

        when(verticalRepo.findById(1)).thenReturn(Optional.of(existingVertical));
        when(verticalRepo.findByVertical("New Vertical")).thenReturn(Optional.empty());
        when(verticalRepo.save(any(MD_Vertical.class))).thenReturn(updatedVertical);

        mockMvc.perform(put("/mdvertical/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updatedVertical)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Vertical name updated from 'Old Vertical' to 'New Vertical'. "))
                .andExpect(jsonPath("$.updatedVertical.vertical").value("New Vertical"));

        verify(verticalRepo, times(1)).findById(1);
        verify(verticalRepo, times(1)).findByVertical("New Vertical");
        verify(verticalRepo, times(1)).save(any(MD_Vertical.class));
    }

    @Test
    void testDeleteMultipleMdVerticals_Success() throws Exception {
        List<Integer> idsToDelete = Arrays.asList(1, 2);
        MD_Vertical vertical1 = new MD_Vertical(1, "Vertical 1");
        MD_Vertical vertical2 = new MD_Vertical(2, "Vertical 2");

        when(verticalRepo.findById(1)).thenReturn(Optional.of(vertical1));
        when(verticalRepo.findById(2)).thenReturn(Optional.of(vertical2));

        mockMvc.perform(delete("/mdvertical/deleteMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(idsToDelete)))
                .andExpect(status().isOk())
                .andExpect(content().string("Verticals '[Vertical 1, Vertical 2]' deleted successfully."));

        verify(verticalRepo, times(1)).findById(1);
        verify(verticalRepo, times(1)).findById(2);
        verify(verticalRepo, times(1)).deleteById(1);
        verify(verticalRepo, times(1)).deleteById(2);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetUniqueData_Success() throws Exception {
        List<MD_Vertical> verticals = Arrays.asList(
                new MD_Vertical(1, "Vertical 1"),
                new MD_Vertical(2, "Vertical 2"),
                new MD_Vertical(3, "Vertical 1")
        );

        when(verticalRepo.findAll()).thenReturn(verticals);

        mockMvc.perform(get("/mdvertical/get"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2)) // Expected unique count
                .andExpect(jsonPath("$[0]").value("Vertical 1"))
                .andExpect(jsonPath("$[1]").value("Vertical 2"));

        verify(verticalRepo, times(1)).findAll();
    }


}