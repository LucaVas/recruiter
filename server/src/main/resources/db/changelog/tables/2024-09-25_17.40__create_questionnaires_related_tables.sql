--liquibase formatted sql
--changeset lucavassos:create_questionnaires_related_tables splitStatements:true

CREATE TABLE questionnaires (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_name VARCHAR(255),

    FOREIGN KEY (client_name) REFERENCES clients(name) ON DELETE SET NULL
);

CREATE TRIGGER update_questionnaires_updated_at
BEFORE UPDATE ON questionnaires
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE questionnaires_history (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    modified_by BIGINT,
    questionnaire_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (modified_by) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (questionnaire_id) REFERENCES questionnaires(id) ON DELETE CASCADE
);
