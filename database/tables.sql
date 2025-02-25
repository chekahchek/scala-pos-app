CREATE TABLE IF NOT EXISTS inventory (
    id          UUID     PRIMARY KEY,
    name        VARCHAR  NOT NULL,
    brand       VARCHAR  NOT NULL,
    category    VARCHAR  NOT NULL,
    quantity    INTEGER  NOT NULL
);