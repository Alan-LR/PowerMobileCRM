ALTER TABLE vehicles
ADD COLUMN brand_id VARCHAR(10) NOT NULL,
ADD COLUMN model_id VARCHAR(20) NOT NULL,
ADD COLUMN fipe_price NUMERIC(15,2),
ADD CONSTRAINT fk_vehicle_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
ADD CONSTRAINT fk_vehicle_model FOREIGN KEY (model_id) REFERENCES models(id);
