package com.sonata.portfoliomanagement.services;

import com.sonata.portfoliomanagement.interfaces.PursuitActionsRepository;
import com.sonata.portfoliomanagement.model.PursuitActions;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PursuitActionsService {


    @Autowired
    private PursuitActionsRepository pursuitActionsRepository;

    @Transactional
    public void deleteByPursuitid(int pursuitid) {
        pursuitActionsRepository.deleteByPursuitid(pursuitid);
    }

    public List<PursuitActions> findByPursuitid(int pursuitid) {
        return pursuitActionsRepository.findByPursuitid(pursuitid);

    }
    public PursuitActions save(PursuitActions pursuitAction) {
        pursuitActionsRepository.save(pursuitAction);
        return pursuitAction;
    }

    public List<PursuitActions> findByActionItemNumber(String actionItemNumber) {
        return pursuitActionsRepository.findByActionItemNumber(actionItemNumber);
    }


    public List<PursuitActions> findAll() {
       return pursuitActionsRepository.findAll();
    }


    public Optional<PursuitActions> findById(int id) {
        return pursuitActionsRepository.findById(id);
    }

    public void delete(PursuitActions pursuitAction) {
        pursuitActionsRepository.delete(pursuitAction);
    }






}
