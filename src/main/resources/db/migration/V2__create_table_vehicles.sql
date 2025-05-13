CREATE TABLE vehicles (
    id SERIAL PRIMARY KEY,
    plate VARCHAR(20) NOT NULL UNIQUE,
    advertised_price NUMERIC(15,2) NOT NULL,
    year INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT,
    CONSTRAINT fk_vehicle_user FOREIGN KEY (user_id) REFERENCES users(id)
);
