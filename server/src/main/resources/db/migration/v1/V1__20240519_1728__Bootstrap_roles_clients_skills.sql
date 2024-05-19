-- Create roles
INSERT INTO roles (name, description, created_dtime, modified_dtime)
VALUES ('RECRUITER', 'Recruiter role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('ADMIN', 'Administrator role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('TESTER', 'Tester role', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Create clients
INSERT INTO clients (name, industry, created_dtime, modified_dtime)
VALUES ('Accenture', 'IT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('IBM', 'IT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Infosys', 'IT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Create skills
INSERT INTO skills (name, created_dtime, modified_dtime)
VALUES ('Java', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Python', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Tableau', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Big Data', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('PL/SQL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Critical Thinking', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Cloud Architecture', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('AWS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       ('Google Cloud', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
