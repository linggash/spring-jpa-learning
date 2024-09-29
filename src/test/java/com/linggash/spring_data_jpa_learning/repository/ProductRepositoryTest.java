package com.linggash.spring_data_jpa_learning.repository;

import com.linggash.spring_data_jpa_learning.entity.Category;
import com.linggash.spring_data_jpa_learning.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.stream.Stream;

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

    @Test
    void testNamedQuery() {
        Pageable pageable = PageRequest.of(0,1);
        List<Product> products = productRepository.searchProductUsingName("Xiaomi 13T", pageable);
        assertEquals(1, products.size());
        assertEquals("Xiaomi 13T", products.get(0).getName());
    }

    @Test
    void searchProducts() {
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Order.desc("id")));
        Page<Product> products = productRepository.searchProduct("%Xiaomi%", pageable);
        assertEquals(1, products.getContent().size());

        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());

        products = productRepository.searchProduct("%GADGET%", pageable);
        assertEquals(1, products.getContent().size());

        assertEquals(0, products.getNumber());
        assertEquals(2, products.getTotalPages());
        assertEquals(2, products.getTotalElements());

    }

    @Test
    void testModifying() {
        int total = productRepository.deleteProductUsingName("Wrong");
        assertEquals(0, total);

        total = productRepository.updateProductPriceToZero(1L);
        assertEquals(1, total);

        Product product = productRepository.findById(1L).orElse(null);
        assertNotNull(product);
        assertEquals(0L, product.getPrice());
    }

    @Test
    void testStream() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Category category = categoryRepository.findById(1L).orElse(null);
            assertNotNull(category);

            Stream<Product> stream = productRepository.streamAllByCategory(category);
            stream.forEach(product -> System.out.println(product.getId() + " : " + product.getName()));
        });
    }

    @Test
    void testSlice() {
        Pageable firstPage = PageRequest.of(0, 1);

        Category category = categoryRepository.findById(1L).orElse(null);
        assertNotNull(category);

        Slice<Product> slice = productRepository.findAllByCategory(category, firstPage);
        // show product content
        while (slice.hasNext()) {
            slice = productRepository.findAllByCategory(category, slice.nextPageable());
        }
    }

    @Test
    void testLock1() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
            assertNotNull(product);
            product.setPrice(30_000_000L);

            try {
                Thread.sleep(20_000L);
                productRepository.save(product);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }


    @Test
    void testLock2() {
        transactionOperations.executeWithoutResult(transactionStatus -> {
            Product product = productRepository.findFirstByIdEquals(1L).orElse(null);
            assertNotNull(product);
            product.setPrice(10_000_000L);
            productRepository.save(product);
        });
    }

    @Test
    void testSpecification() {
        Specification<Product> specification = ((root, criteriaQuery, criteriaBuilder) -> {
            return criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("name"), "Xiaomi 13T"),
                            criteriaBuilder.equal(root.get("name"), "Xiaomi 14T")
                    )
            ).getRestriction();
        });

        List<Product> products = productRepository.findAll(specification);
        assertEquals(2, products.size());
    }
}