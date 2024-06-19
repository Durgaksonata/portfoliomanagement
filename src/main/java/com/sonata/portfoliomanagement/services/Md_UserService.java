package com.sonata.portfoliomanagement.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonata.portfoliomanagement.interfaces.MD_DeliveryDirectorRepository;
import com.sonata.portfoliomanagement.interfaces.MD_DeliveryManagerRepository;
import com.sonata.portfoliomanagement.interfaces.MD_ProjectManagerRepository;
import com.sonata.portfoliomanagement.interfaces.MD_UsersRepository;
import com.sonata.portfoliomanagement.model.MD_DeliveryDirector;
import com.sonata.portfoliomanagement.model.MD_DeliveryManager;
import com.sonata.portfoliomanagement.model.MD_ProjectManager;
import com.sonata.portfoliomanagement.model.MD_Users;

import jakarta.transaction.Transactional;
@Service
public class Md_UserService {
    @Autowired
    private MD_UsersRepository usersRepository;

    @Autowired
    private MD_DeliveryDirectorRepository deliveryDirectorRepository;

    @Autowired
    private MD_DeliveryManagerRepository deliveryManagerRepository;

    @Autowired
    private MD_ProjectManagerRepository projectManagerRepository;

    @Transactional
    public MD_Users createUserWithRole(MD_Users user) {
        MD_Users createdUser = usersRepository.save(user);
        List<String> roles = user.getRole();


        //switch (user.getRole())
        for (String role : roles) {
            switch (role){
                case "DeliveryDirector":
                    createDeliveryDirector(user);
                    break;
                case "DeliveryManager":
                    createDeliveryManager(user);
                    break;
                case "ProjectManager":
                    createProjectManager(user);
                    break;
                default:
                    break;
                // Assign user ID to related entities based on role
//        switch (user.getRoles()) {
//            case "DeliveryDirector":
//            	MD_DeliveryDirector deliveryDirector = new MD_DeliveryDirector();
//                // Set other fields as needed for delivery director
//                createdUser.setDeliveryDirector(deliveryDirector);	                break;
//            case "DeliveryManager":
//                createdUser.setDeliveryManager(deliveryManagerRepository.save(new MD_DeliveryManager(createdUser)));
//                break;
//            case "ProjectManager":
//                createdUser.setProjectManager(projectManagerRepository.save(new MD_ProjectManager(createdUser)));
//                break;
//            // Add cases for other roles as needed
//            default:
//                break;
            }
        }



        return createdUser;
    }


    @Transactional
    public void createDeliveryDirector(MD_Users user) {
        MD_DeliveryDirector deliveryDirector = new MD_DeliveryDirector();
        deliveryDirector.setDeliveryDirector(user.getFirstName() + " " + user.getLastName());
        deliveryDirectorRepository.save(deliveryDirector);
    }

    @Transactional
    public void createDeliveryManager(MD_Users user) {
        MD_DeliveryManager deliveryManager = new MD_DeliveryManager();
        deliveryManager.setDelivery_Managers(user.getFirstName() + " " + user.getLastName());
        deliveryManagerRepository.save(deliveryManager);
    }

    @Transactional
    public void createProjectManager(MD_Users user) {
        MD_ProjectManager projectManager = new MD_ProjectManager();
        projectManager.setProjectManager(user.getFirstName() + " " + user.getLastName());

        projectManagerRepository.save(projectManager);
    }


    @Transactional
    public void deleteRelatedEntities(MD_Users user) {
        List<String> roles = user.getRole();
        for (String role : roles) {
            if ("DeliveryDirector".equals(role)) {
                deleteDeliveryDirector(user.getFirstName() + " " + user.getLastName());
            } else if ("DeliveryManager".equals(role)) {
                deleteDeliveryManager(user.getFirstName() + " " + user.getLastName());
            } else if ("ProjectManager".equals(role)) {
                deleteProjectManager(user.getFirstName() + " " + user.getLastName());
            }
            // Add more if-else statements as needed for other roles
        }
    }

    @Transactional
    public void deleteDeliveryDirector(String deliveryDirector) {
        deliveryDirectorRepository.deleteByDeliveryDirector(deliveryDirector);
    }

    @Transactional
    public void deleteDeliveryManager(String deliveryManager) {
        deliveryManagerRepository.deleteByDeliveryManagers(deliveryManager);
    }

    @Transactional
    public void deleteProjectManager(String projectManager) {
        projectManagerRepository.deleteByProjectManager(projectManager);
    }


}