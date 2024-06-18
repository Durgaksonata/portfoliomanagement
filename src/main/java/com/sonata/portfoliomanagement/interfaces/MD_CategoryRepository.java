package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MD_CategoryRepository extends JpaRepository<MD_Category, Integer> {
    Optional<MD_Category> findByCategory(String category);
}
