package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PursuitActionsService {


    @Autowired
    private PursuitActionsRepository pursuitActionsRepository;

    @Transactional
    public void deleteByPursuitid(String pursuitid) {
        pursuitActionsRepository.deleteByPursuitid(pursuitid);
    }

    public List<PursuitActions> findByPursuitid(String pursuitid) {
        return pursuitActionsRepository.findByPursuitid(pursuitid);

    }
    public void save(PursuitActions pursuitAction) {
        pursuitActionsRepository.save(pursuitAction);
    }
}