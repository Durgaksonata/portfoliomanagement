package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmailAndPassword(String email, String password);

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    boolean existsByEmail(String email);
}
