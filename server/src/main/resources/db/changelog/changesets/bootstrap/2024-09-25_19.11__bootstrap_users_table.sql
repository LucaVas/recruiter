--liquibase formatted sql
--changeset lucavassos:bootstrap_users_table splitStatements:true

INSERT INTO users (name, email, password, phone, city, country, approved, approved_at, role_id)
VALUES
    ('recruiter', 'recruiter@mail.com', 'encodedPassword1', '1234567890', 'Test city', 'India', TRUE, CURRENT_TIMESTAMP, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('recruiter2', 'recruiter2@mail.com', 'encodedPassword1', '1234567892', 'Test city 2', 'India', FALSE, NULL, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('recruiter3', 'recruiter3@mail.com', 'encodedPassword1', '1234567893', 'Test city 3', 'India', FALSE, NULL, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('recruiter4', 'recruiter4@mail.com', 'encodedPassword1', '1234567894', 'Test city 4', 'India', FALSE, NULL, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('recruiter5', 'recruiter5@mail.com', 'encodedPassword1', '1234567895', 'Test city 5', 'India', FALSE, NULL, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('recruiter6', 'recruiter6@mail.com', 'encodedPassword1', '1234567896', 'Test city 6', 'India', FALSE, NULL, (SELECT id FROM roles WHERE name = 'RECRUITER')),
    ('admin', 'admin@mail.com', 'encodedPassword1', '1234567891', 'Test city', 'India', TRUE, CURRENT_TIMESTAMP, (SELECT id FROM roles WHERE name = 'ADMIN'));

--rollback DELETE FROM users WHERE email IN ('recruiter@mail.com', 'recruiter2@mail.com', 'recruiter3@mail.com', 'recruiter4@mail.com', 'recruiter5@mail.com', 'recruiter6@mail.com', 'admin@mail.com');
