package com.linggash.spring_data_jpa_learning.repository;

import com.linggash.spring_data_jpa_learning.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
