CREATE TABLE IF NOT EXISTS inventory (
    name        VARCHAR  NOT NULL,
    brand       VARCHAR  NOT NULL,
    category    VARCHAR  NOT NULL,
    quantity    INTEGER  NOT NULL
);

INSERT INTO inventory(name, brand, category, quantity)
VALUES
    ('Apple', 'Fruits&Co.', 'Fruits', 10),
    ('Orange', 'Fruits&Co.', 'Fruits', 3),
    ('Pear', 'Fruits&Co.', 'Fruits', 7),
    ('WaterMelon', 'Fruits&Co.', 'Fruits', 2);

