--liquibase formatted sql
--changeset lucavassos:create_users_related_table splitStatements:true

CREATE TABLE users (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            name VARCHAR(50) NOT NULL UNIQUE,
            email VARCHAR(50) NOT NULL UNIQUE,
            password VARCHAR(64) NOT NULL,
            phone VARCHAR(10) NOT NULL UNIQUE,
            city VARCHAR(50) NOT NULL,
            country VARCHAR(50) NOT NULL,
            comment VARCHAR(255),
            approved BOOLEAN NOT NULL DEFAULT FALSE,
            approved_at TIMESTAMP,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            approver_id BIGINT,
            role_id BIGINT NOT NULL,

            CONSTRAINT fk_approver FOREIGN KEY (approver_id) REFERENCES users(id),
            CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles(id)
        );

CREATE TRIGGER update_users_updated_at
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE users_history (
            event_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            phone VARCHAR(50) NOT NULL,
            city VARCHAR(50) NOT NULL,
            country VARCHAR(50) NOT NULL,
            approved BOOLEAN NOT NULL,
            event_type VARCHAR(50) NOT NULL,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            user_id BIGINT,
            modified_by_id BIGINT,

            CONSTRAINT fk_users_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
            CONSTRAINT fk_users_history_modified_by FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
        );
