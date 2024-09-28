CREATE DATABASE spring_data_jpa_learning;

USE spring_data_jpa_learning;

CREATE TABLE categories(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)ENGINE = InnoDB;

SElECT * FROM categories;

CREATE TABLE products(
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    name        VARCHAR(100)    NOT NULL,
    price       BIGINT          NOT NULL,
    category_id BIGINT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY fk_products_categories (category_id) REFERENCES categories (id)
) ENGINE InnoDB;

SElECT * FROM products;

ALTER TABLE categories
    ADD COLUMN created_date TIMESTAMP;

ALTER TABLE categories
    ADD COLUMN last_modified_date TIMESTAMP;
