package com.linggash.spring_data_jpa_learning.service;

import com.linggash.spring_data_jpa_learning.entity.Category;
import com.linggash.spring_data_jpa_learning.entity.Product;
import com.linggash.spring_data_jpa_learning.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void createProducts() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        {
            Product product = new Product();
            product.setName("Xiaomi 13T");
            product.setPrice(6_500_000L);
            product.setCategory(category);
            productRepository.save(product);
        }

        {
            Product product = new Product();
            product.setName("Xiaomi 14T");
            product.setPrice(7_500_000L);
            product.setCategory(category);
            productRepository.save(product);
        }
    }

    @Test
    void findByCategoryName() {
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH");
        assertEquals(2, products.size());
        assertEquals("Xiaomi 13T", products.get(0).getName());
        assertEquals("Xiaomi 14T", products.get(1).getName());
    }

    @Test
    void sort() {
        Sort sort = Sort.by(Sort.Order.desc("id"));
        List<Product> products = productRepository.findAllByCategory_Name("GADGET MURAH", sort);
        assertEquals(2, products.size());
        assertEquals("Xiaomi 14T", products.get(0).getName());
        assertEquals("Xiaomi 13T", products.get(1).getName());
    }

    @Test
    void testPageable() {
        // page 0
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        List<Product> page1 = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, page1.size());
        assertEquals("Xiaomi 14T", page1.get(0).getName());

        // page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        List<Product> page2 = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, page2.size());
        assertEquals("Xiaomi 13T", page2.get(0).getName());
    }
}