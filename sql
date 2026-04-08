CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    age INTEGER,
    created_at TIMESTAMP
);

CREATE TABLE processed_events (
    event_id UUID PRIMARY KEY,
    processed_at TIMESTAMP NOT NULL
);

INSERT INTO users (name, email, age, created_at)
VALUES
('Alice', 'alice@mail.ru', 25, NOW()),
('Bob', 'bob@mail.ru', 30, NOW()),
('Charlie', 'charlie@mail.ru', 28, NOW());

SELECT * FROM users;