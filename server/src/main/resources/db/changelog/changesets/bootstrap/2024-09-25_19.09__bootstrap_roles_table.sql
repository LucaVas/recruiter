--liquibase formatted sql
--changeset lucavassos:bootstrap_roles_table splitStatements:true

INSERT INTO roles (name, description)
VALUES
    ('RECRUITER', 'Recruiter role'),
    ('ADMIN', 'Administrator role'),
    ('TESTER', 'Tester role');

--rollback DELETE FROM roles WHERE name IN ('RECRUITER', 'ADMIN', 'TESTER');
