--liquibase formatted sql
--changeset lucavassos:create_candidates_related_table splitStatements:true


CREATE TABLE candidates (
    pan VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(10) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    total_experience DOUBLE PRECISION NOT NULL,
    education VARCHAR(255) NOT NULL,
    current_ctc DOUBLE PRECISION NOT NULL,
    candidate_status VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    recruiter_id BIGINT,

    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TRIGGER update_candidates_updated_at
BEFORE UPDATE ON candidates
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TABLE candidates_history (
    event_id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    pan VARCHAR(10) NOT NULL,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(10) NOT NULL,
    email VARCHAR(255) NOT NULL,
    total_experience DOUBLE PRECISION NOT NULL,
    education VARCHAR(255) NOT NULL,
    current_ctc DOUBLE PRECISION NOT NULL,
    candidate_status VARCHAR(50) DEFAULT NULL,
    event_type VARCHAR(50) NOT NULL,
    candidate_id VARCHAR(10) DEFAULT NULL,
    modified_by_id BIGINT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (candidate_id) REFERENCES candidates(pan) ON DELETE CASCADE,
    FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
);

--rollback DROP TABLE IF EXISTS candidates_history;
--rollback DROP TRIGGER IF EXISTS update_candidates_updated_at ON candidates;
--rollback DROP TABLE IF EXISTS candidates;
