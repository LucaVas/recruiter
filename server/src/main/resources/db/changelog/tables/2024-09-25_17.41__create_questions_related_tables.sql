--liquibase formatted sql
--changeset lucavassos:create_questions_related_tables splitStatements:true


CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    answer VARCHAR(500),
    type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    questionnaire_id BIGINT,

    FOREIGN KEY (questionnaire_id) REFERENCES questionnaires(id) ON DELETE CASCADE
);

CREATE TRIGGER update_questions_updated_at
BEFORE UPDATE ON questions
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE questions_history (
    event_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    text VARCHAR(255) NOT NULL,
    answer VARCHAR(500),
    type VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    question_id BIGINT,
    modified_by_id BIGINT,

    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE,
    FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
);
