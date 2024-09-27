package com.linggash.spring_data_jpa_learning.repository;

import com.linggash.spring_data_jpa_learning.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findFirstByName(String name);

    List<Category> findAllByNameLike(String name);

}
