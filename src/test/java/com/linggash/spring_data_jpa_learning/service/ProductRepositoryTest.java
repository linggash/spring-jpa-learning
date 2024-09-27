package com.linggash.spring_data_jpa_learning.service;

import com.linggash.spring_data_jpa_learning.entity.Category;
import com.linggash.spring_data_jpa_learning.entity.Product;
import com.linggash.spring_data_jpa_learning.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TransactionOperations transactionOperations;

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
        Page<Product> page1 = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, page1.getContent().size());
        assertEquals(0, page1.getNumber());
        assertEquals(2, page1.getTotalElements());
        assertEquals(2, page1.getTotalPages());
        assertEquals("Xiaomi 14T", page1.getContent().get(0).getName());

        // page 1
        pageable = PageRequest.of(1, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> page2 = productRepository.findAllByCategory_Name("GADGET MURAH", pageable);
        assertEquals(1, page2.getContent().size());
        assertEquals(1, page2.getNumber());
        assertEquals(2, page2.getTotalElements());
        assertEquals(2, page2.getTotalPages());
        assertEquals("Xiaomi 13T", page2.getContent().get(0).getName());
    }

    @Test
    void testCountByCategory_Name() {
        Long count = productRepository.count();
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("GADGET MURAH");
        assertEquals(2L, count);

        count = productRepository.countByCategory_Name("NOTHINNGG");
        assertEquals(0L, count);
    }

    @Test
    void testExistByName() {
        boolean exists = productRepository.existsByName("Xiaomi 13T");
        assertTrue(exists);

        exists = productRepository.existsByName("SIOMAY 13T");
        assertFalse(exists);
    }

    @Test
    void testDeleteByNameOld() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Product product = new Product();
            product.setName("Vivo X100");
            product.setPrice(13_000_000L);
            product.setCategory(category);
            productRepository.save(product);

            int delete = productRepository.deleteByName("Vivo X100");
            assertEquals(1, delete);

            delete = productRepository.deleteByName("Vivo X100");
            assertEquals(0, delete);
        });
    }


    @Test
    void testDeleteByNameNew() {
        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Product product = new Product();
        product.setName("Vivo X100");
        product.setPrice(13_000_000L);
        product.setCategory(category);
        productRepository.save(product);

        int delete = productRepository.deleteByName("Vivo X100");
        assertEquals(1, delete);

        delete = productRepository.deleteByName("Vivo X100");
        assertEquals(0, delete);
    }
}