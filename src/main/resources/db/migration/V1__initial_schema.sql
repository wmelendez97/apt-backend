-- Customer table
CREATE TABLE customer (
                          id_customer BIGSERIAL PRIMARY KEY,
                          name VARCHAR(150) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          phone VARCHAR(20),
                          password VARCHAR(255) NOT NULL,
                          active BOOLEAN NOT NULL DEFAULT TRUE,
                          created_by VARCHAR(50),
                          created_at TIMESTAMP,
                          updated_by VARCHAR(50),
                          updated_at TIMESTAMP
);

-- Order table
CREATE TABLE orders (
                        id_order BIGSERIAL PRIMARY KEY,
                        customer_id BIGINT NOT NULL,
                        status VARCHAR(50) NOT NULL,
                        total DECIMAL(10,2),
                        created_by VARCHAR(50),
                        created_at TIMESTAMP,
                        updated_by VARCHAR(50),
                        updated_at TIMESTAMP,
                        CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id_customer)
);

-- Order detail table
CREATE TABLE order_detail (
                              id_order_detail BIGSERIAL PRIMARY KEY,
                              order_id BIGINT NOT NULL,
                              product_id BIGINT NOT NULL,
                              product_title VARCHAR(255),
                              price DECIMAL(10,2) NOT NULL,
                              quantity INTEGER NOT NULL,
                              subtotal DECIMAL(10,2) NOT NULL,
                              created_by VARCHAR(50),
                              created_at TIMESTAMP,
                              updated_by VARCHAR(50),
                              updated_at TIMESTAMP,
                              CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id_order)
);

-- Payment table
CREATE TABLE payment (
                         id_payment BIGSERIAL PRIMARY KEY,
                         order_id BIGINT NOT NULL,
                         status VARCHAR(50) NOT NULL,
                         payment_method VARCHAR(50) NOT NULL,
                         amount DECIMAL(10,2) NOT NULL,
                         transaction_id VARCHAR(100) NOT NULL UNIQUE,
                         created_by VARCHAR(50),
                         created_at TIMESTAMP,
                         updated_by VARCHAR(50),
                         updated_at TIMESTAMP,
                         CONSTRAINT fk_order_payment FOREIGN KEY (order_id) REFERENCES orders(id_order)
);