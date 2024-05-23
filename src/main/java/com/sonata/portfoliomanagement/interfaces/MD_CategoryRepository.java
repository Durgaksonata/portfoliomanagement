package com.sonata.portfoliomanagement.interfaces;

import com.sonata.portfoliomanagement.model.MD_Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MD_CategoryRepository extends JpaRepository<MD_Category, Integer> {
}
