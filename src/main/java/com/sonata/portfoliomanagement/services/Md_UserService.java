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

        for (String role : roles) {
            switch (role) {
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
        String oldFullName = existingUser.getFirstName() + " " + existingUser.getLastName();

        // Remove roles that are no longer assigned
        for (String role : existingRoles) {
            if (!updatedRoles.contains(role)) {
                switch (role) {
                    case "Delivery Director":
                        deleteDeliveryDirector(oldFullName);
                        break;
                    case "Delivery Manager":
                        deleteDeliveryManager(oldFullName);
                        break;
                    case "Project Manager":
                        deleteProjectManager(oldFullName);
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
                    case "Delivery Director":
                        createDeliveryDirector(updatedUser);
                        break;
                    case "Delivery Manager":
                        createDeliveryManager(updatedUser);
                        break;
                    case "Project Manager":
                        createProjectManager(updatedUser);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Transactional
    public void updateRoleEntities(MD_Users user, String oldFullName, String newFullName) {
        List<String> roles = user.getRole();
        for (String role : roles) {
            switch (role) {
                case "Delivery Director":
                    updateDeliveryDirectorName(oldFullName, newFullName);
                    break;
                case "Delivery Manager":
                    updateDeliveryManagerName(oldFullName, newFullName);
                    break;
                case "Project Manager":
                    updateProjectManagerName(oldFullName, newFullName);
                    break;
                default:
                    break;
            }
        }
    }

    @Transactional
    public void updateDeliveryDirectorName(String oldFullName, String newFullName) {
        List<MD_DeliveryDirector> deliveryDirectors = deliveryDirectorRepository.findByDeliveryDirector(oldFullName);
        for (MD_DeliveryDirector deliveryDirector : deliveryDirectors) {
            deliveryDirector.setDeliveryDirector(newFullName);
            deliveryDirectorRepository.save(deliveryDirector);
        }
    }

    @Transactional
    public void updateDeliveryManagerName(String oldFullName, String newFullName) {
        List<MD_DeliveryManager> deliveryManagers = deliveryManagerRepository.findByDeliveryManagers(oldFullName);
        for (MD_DeliveryManager deliveryManager : deliveryManagers) {
            deliveryManager.setDelivery_Managers(newFullName);
            deliveryManagerRepository.save(deliveryManager);
        }
    }

    @Transactional
    public void updateProjectManagerName(String oldFullName, String newFullName) {
        List<MD_ProjectManager> projectManagers = projectManagerRepository.findByProjectManager(oldFullName);
        for (MD_ProjectManager projectManager : projectManagers) {
            projectManager.setProjectManager(newFullName);
            projectManagerRepository.save(projectManager);
        }
    }

    @Transactional
    public void deleteRelatedEntities(MD_Users user) {
        List<String> roles = user.getRole();
        String fullName = user.getFirstName() + " " + user.getLastName();
        for (String role : roles) {
            switch (role) {
                case "Delivery Director":
                    deleteDeliveryDirector(fullName);
                    break;
                case "Delivery Manager":
                    deleteDeliveryManager(fullName);
                    break;
                case "Project Manager":
                    deleteProjectManager(fullName);
                    break;
                default:
                    break;
            }
        }
    }

}