CREATE TABLE url (
    hash VARCHAR(6) PRIMARY KEY,
    url VARCHAR(255) UNIQUE ,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

CREATE TABLE hash (
    hash VARCHAR(6) PRIMARY KEY
)