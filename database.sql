CREATE DATABASE spring_data_jpa_learning;

USE spring_data_jpa_learning;

CREATE TABLE categories(
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)ENGINE = InnoDB;

SElECT * FROM categories;