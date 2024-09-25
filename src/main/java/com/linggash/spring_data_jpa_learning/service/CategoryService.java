package com.linggash.spring_data_jpa_learning.service;

import com.linggash.spring_data_jpa_learning.entity.Category;
import com.linggash.spring_data_jpa_learning.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {


    private CategoryRepository categoryRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public void create() {
        for (int i = 0; i < 5; i++) {
            Category category = new Category();
            category.setName("Category" + i);
            categoryRepository.save(category);
        }
        throw new RuntimeException("Ups rollback please");
    }

    @Transactional
    public  void test() {
        create();
    }

}
