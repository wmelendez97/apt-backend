-- Insert customer (password encrypted with BCrypt for 'pruebaApt123')
INSERT INTO customer (name, email, phone, password, active, created_by, created_at)
VALUES ('William Melendez', 'testapt@yopmail.com', '21212121', '$2a$12$wJZyuErQXbztTwO4WvmOp.5eb/1MYpuojP/BanZXIwS3y0zGTQHTi', TRUE, 'system', '2026-04-10 17:40:00');

-- Insert order
INSERT INTO orders (customer_id, status, total, created_by, created_at, updated_by, updated_at)
VALUES (1, 'PAID', 28.97, 'testapt@yopmail.com', '2026-04-10 17:41:00.500579', 'testapt@yopmail.com', '2026-04-10 17:41:27.880924');

-- Insert order details
INSERT INTO order_detail (order_id, product_id, product_title, price, quantity, subtotal, created_by, created_at)
VALUES
    (1, 1, 'Essence Mascara Lash Princess', 9.99, 2, 19.98, 'testapt@yopmail.com', '2026-04-10 17:41:01.580563'),
    (1, 5, 'Red Nail Polish', 8.99, 1, 8.99, 'testapt@yopmail.com', '2026-04-10 17:41:01.718847');

-- Insert payment
INSERT INTO payment (order_id, status, payment_method, amount, transaction_id, created_by, created_at)
VALUES (1, 'APPROVED', 'CREDIT_CARD', 28.97, '6f43abda-12f5-4cb9-ba0d-ccba7fbcca45', 'testapt@yopmail.com', '2026-04-10 17:41:27.877076');