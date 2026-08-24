CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    price NUMERIC(12,2) NOT NULL CHECK (price > 0),
    inventory INTEGER NOT NULL CHECK (inventory >= 0),
    category VARCHAR(80) NOT NULL
);
CREATE INDEX idx_products_category ON products(category);

INSERT INTO products(name, description, price, inventory, category) VALUES
('Wireless Headphones','Noise cancelling Bluetooth headphones',7999.00,25,'Electronics'),
('Mechanical Keyboard','RGB mechanical keyboard with hot-swap switches',5499.00,40,'Electronics'),
('Running Shoes','Lightweight everyday running shoes',3999.00,30,'Footwear'),
('Travel Backpack','Water-resistant 30L laptop backpack',2999.00,35,'Accessories');
