--liquibase formatted sql
--changeset lucavassos:create_password_reset_tokens_table splitStatements:true

CREATE TABLE password_reset_tokens (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    token_string VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    user_id BIGINT,

    CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TRIGGER update_password_reset_tokens_updated_at
BEFORE UPDATE ON password_reset_tokens
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_roles_updated_at ON password_reset_tokens;
--rollback DROP TABLE IF EXISTS password_reset_tokens;
