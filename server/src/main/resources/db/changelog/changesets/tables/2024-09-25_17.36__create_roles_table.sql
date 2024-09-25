--liquibase formatted sql
--changeset lucavassos:create_roles_table splitStatements:true

CREATE TABLE roles (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            name VARCHAR(60) NOT NULL UNIQUE,
            description TEXT NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
        );

CREATE TRIGGER update_roles_updated_at
BEFORE UPDATE ON roles
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_roles_updated_at ON roles;
--rollback DROP TABLE IF EXISTS roles;
