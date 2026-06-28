INSERT INTO users (email, username, phone_number, password, role, provider, provider_id)
VALUES (
    'raatbekjumabekovv@gmail.com',
    'raatbek',
    '+996707900536',
    '$2a$10$BrgK7LZ.3byQXzM6qzzIOu0xK7x8wE0sG.xsS1UQJxZy3ysp6u7bC', -- BCrypt for '123456'
    'ADMIN',
    'LOCAL',
    NULL
)
ON CONFLICT (email) DO NOTHING;
