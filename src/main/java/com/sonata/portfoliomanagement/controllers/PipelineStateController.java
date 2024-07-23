package com.sonata.portfoliomanagement.controllers;

import com.sonata.portfoliomanagement.interfaces.DataEntryRepository;
import com.sonata.portfoliomanagement.interfaces.PipelineStateRepository;
import com.sonata.portfoliomanagement.model.DataEntry;
import com.sonata.portfoliomanagement.model.PipelineState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173" )
@RestController
@RequestMapping("Pipelinestate")
public class PipelineStateController {

    @Autowired
    private PipelineStateRepository pipelineStateRepository;

    @Autowired
    private DataEntryRepository dataEntryRepository;

    @GetMapping("/get")
    public List<PipelineState> getPipelineState(){
        return pipelineStateRepository.findAll();
    }


    @PostMapping("/save")
    public PipelineState savePipelineState(@RequestBody PipelineState pipelineState) {
        return pipelineStateRepository.save(pipelineState);
    }




}
