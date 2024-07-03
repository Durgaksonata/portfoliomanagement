package com.sonata.portfoliomanagement.JunitTest;



import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.util.*;

import com.sonata.portfoliomanagement.controllers.MD_ClassificationController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sonata.portfoliomanagement.interfaces.MD_ClassificationRepository;
import com.sonata.portfoliomanagement.model.MD_Classification;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MD_ClassificationController.class)
public class MD_ClassificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MD_ClassificationRepository classificationRepo;

    @InjectMocks
    private MD_ClassificationController classificationController;

    @Test
    public void testGetAllData() throws Exception {
        List<MD_Classification> classifications = Arrays.asList(
                new MD_Classification(1, "Classification1"),
                new MD_Classification(2, "Classification2")
        );

        when(classificationRepo.findAll()).thenReturn(classifications);

        mockMvc.perform(MockMvcRequestBuilders.get("/mdclassification/getall")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(classifications.size()));
    }

    @Test
    public void testGetUniqueData() throws Exception {
        List<MD_Classification> classifications = Arrays.asList(
                new MD_Classification(1, "Classification1"),
                new MD_Classification(2, "Classification2"),
                new MD_Classification(3, "Classification1")
        );

        when(classificationRepo.findAll()).thenReturn(classifications);

        mockMvc.perform(MockMvcRequestBuilders.get("/mdclassification/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2)); // Expecting two unique classifications
    }

    @Test
    public void testCreateMdClassification() throws Exception {
        MD_Classification newClassification = new MD_Classification(null, "New Classification");

        when(classificationRepo.save(any(MD_Classification.class))).thenReturn(new MD_Classification(1, "New Classification"));

        mockMvc.perform(MockMvcRequestBuilders.post("/mdclassification/save")
                        .content(new ObjectMapper().writeValueAsString(newClassification))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testUpdateMdClassification() throws Exception {
        MD_Classification updatedClassification = new MD_Classification(1, "Updated Classification");

        when(classificationRepo.findById(anyInt())).thenReturn(Optional.of(new MD_Classification(1, "Old Classification")));
        when(classificationRepo.save(any(MD_Classification.class))).thenReturn(updatedClassification);

        mockMvc.perform(MockMvcRequestBuilders.put("/mdclassification/update")
                        .content(new ObjectMapper().writeValueAsString(updatedClassification))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testDeleteClassificationsByIds() throws Exception {
        // IDs to delete
        List<Integer> idsToDelete = Arrays.asList(1, 2);

        // Mocking findById and deleteById methods in the repository
        when(classificationRepo.findById(1)).thenReturn(Optional.of(new MD_Classification(1, "Classification1")));
        when(classificationRepo.findById(2)).thenReturn(Optional.of(new MD_Classification(2, "Classification2")));

        // Mocking the deleteById method
        doNothing().when(classificationRepo).deleteById(any(Integer.class));

        // Performing the delete request
        mockMvc.perform(MockMvcRequestBuilders.delete("/mdclassification/delete")
                        .content(new ObjectMapper().writeValueAsString(idsToDelete))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verifying that deleteById was called for each ID in idsToDelete
        verify(classificationRepo, times(1)).deleteById(1);
        verify(classificationRepo, times(1)).deleteById(2);
    }
}