CREATE TABLE brands (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE models (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand_id VARCHAR(10) NOT NULL,
    CONSTRAINT fk_model_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
);
