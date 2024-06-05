package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.MD_PursuitProbabilityRepository;
import com.sonata.portfoliomanagement.model.MD_PursuitProbability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PursuitTrackerService {

    @Autowired
    private MD_PursuitProbabilityRepository mdPursuitProbabilityRepository;

    // Method to calculate stage based on PursuitStatus and Type
    public String calculateStage(String pursuitStatus, String type) {
        if (pursuitStatus == null || pursuitStatus.isEmpty() || type == null || type.isEmpty()) {
            return "";
        }

        Optional<MD_PursuitProbability> result = mdPursuitProbabilityRepository.findByPursuitStatusAndType(pursuitStatus, type);
        return result.map(MD_PursuitProbability::getStage).orElse("");
    }

    // Method to calculate pursuitProbability based on PursuitStatus and Type
    public int calculatePursuitProbability(String pursuitStatus, String type) {
        if (pursuitStatus == null || pursuitStatus.isEmpty() || type == null || type.isEmpty()) {
            return 0;
        }

        Optional<MD_PursuitProbability> result = mdPursuitProbabilityRepository.findByPursuitStatusAndType(pursuitStatus, type);
        return result.map(MD_PursuitProbability::getProbability).orElse(0);
    }
}

