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

    public boolean existsDeliveryDirector(int userId) {
        return deliveryDirectorRepository.existsById(userId);
    }

    @Transactional
    public void updateDeliveryDirector(MD_Users user) {
        MD_DeliveryDirector deliveryDirector = deliveryDirectorRepository.findById(user.getId()).orElseThrow();
        deliveryDirector.setDeliveryDirector(user.getFirstName() + " " + user.getLastName());
        deliveryDirectorRepository.save(deliveryDirector);
    }

    public boolean existsDeliveryManager(int userId) {
        return deliveryManagerRepository.existsById(userId);
    }

    @Transactional
    public void updateDeliveryManager(MD_Users user) {
        MD_DeliveryManager deliveryManager = deliveryManagerRepository.findById(user.getId()).orElseThrow();
        deliveryManager.setDelivery_Managers(user.getFirstName() + " " + user.getLastName());
        deliveryManagerRepository.save(deliveryManager);
    }

    public boolean existsProjectManager(int userId) {
        return projectManagerRepository.existsById(userId);
    }

    @Transactional
    public void updateProjectManager(MD_Users user) {
        MD_ProjectManager projectManager = projectManagerRepository.findById(user.getId()).orElseThrow();
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


    @Transactional
    public void updateRoles(MD_Users existingUser, MD_Users updatedUser) {
        List<String> existingRoles = existingUser.getRole();
        List<String> updatedRoles = updatedUser.getRole();

        // Remove roles that are no longer assigned
        for (String role : existingRoles) {
            if (!updatedRoles.contains(role)) {
                switch (role) {
                    case "DeliveryDirector":
                        deleteDeliveryDirector(existingUser.getFirstName() + " " + existingUser.getLastName());
                        break;
                    case "DeliveryManager":
                        deleteDeliveryManager(existingUser.getFirstName() + " " + existingUser.getLastName());
                        break;
                    case "ProjectManager":
                        deleteProjectManager(existingUser.getFirstName() + " " + existingUser.getLastName());
                        break;
                    default:
                        break;
                }
            }
        }

        // Add new roles
        for (String role : updatedRoles) {
            if (!existingRoles.contains(role)) {
                switch (role) {
                    case "DeliveryDirector":
                        createDeliveryDirector(updatedUser);
                        break;
                    case "DeliveryManager":
                        createDeliveryManager(updatedUser);
                        break;
                    case "ProjectManager":
                        createProjectManager(updatedUser);
                        break;
                    default:
                        break;
                }
            }
        }
    }


    @Transactional
    public void updateRoleEntities(MD_Users user) {
        List<String> roles = user.getRole();
        for (String role : roles) {
            switch (role) {
                case "DeliveryDirector":
                    updateDeliveryDirectorName(user);
                    break;
                case "DeliveryManager":
                    updateDeliveryManagerName(user);
                    break;
                case "ProjectManager":
                    updateProjectManagerName(user);
                    break;
                default:
                    break;
            }
        }
    }

    @Transactional
    public void updateDeliveryDirectorName(MD_Users user) {
        List<MD_DeliveryDirector> deliveryDirectors = deliveryDirectorRepository.findByDeliveryDirector(user.getFirstName() + " " + user.getLastName());
        for (MD_DeliveryDirector deliveryDirector : deliveryDirectors) {
            deliveryDirector.setDeliveryDirector(user.getFirstName() + " " + user.getLastName());
            deliveryDirectorRepository.save(deliveryDirector);
        }
    }

    @Transactional
    public void updateDeliveryManagerName(MD_Users user) {
        List<MD_DeliveryManager> deliveryManagers = deliveryManagerRepository.findByDeliveryManagers(user.getFirstName() + " " + user.getLastName());
        for (MD_DeliveryManager deliveryManager : deliveryManagers) {
            deliveryManager.setDelivery_Managers(user.getFirstName() + " " + user.getLastName());
            deliveryManagerRepository.save(deliveryManager);
        }
    }

    @Transactional
    public void updateProjectManagerName(MD_Users user) {
        List<MD_ProjectManager> projectManagers = projectManagerRepository.findByProjectManager(user.getFirstName() + " " + user.getLastName());
        for (MD_ProjectManager projectManager : projectManagers) {
            projectManager.setProjectManager(user.getFirstName() + " " + user.getLastName());
            projectManagerRepository.save(projectManager);
        }
    }




}