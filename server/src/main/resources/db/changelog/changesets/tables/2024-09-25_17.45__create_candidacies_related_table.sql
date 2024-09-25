--liquibase formatted sql
--changeset lucavassos:create_candidacies_related_table splitStatements:true

CREATE TABLE candidacies (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    relevant_experience DOUBLE PRECISION NOT NULL,
    expected_ctc DOUBLE PRECISION NOT NULL,
    official_notice_period DOUBLE PRECISION NOT NULL,
    actual_notice_period DOUBLE PRECISION NOT NULL,
    reason_for_quick_join VARCHAR(255),
    candidacy_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    job_id BIGINT DEFAULT NULL,
    candidate_pan VARCHAR(10) DEFAULT NULL,
    recruiter_id BIGINT DEFAULT NULL,

    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE SET NULL,
    FOREIGN KEY (candidate_pan) REFERENCES candidates(pan) ON DELETE SET NULL,
    FOREIGN KEY (recruiter_id) REFERENCES users(id) ON DELETE SET NULL
);

CREATE TRIGGER update_candidacies_updated_at
BEFORE UPDATE ON candidacies
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE candidacy_history (
    event_id UUID NOT NULL PRIMARY KEY,
    relevant_experience DOUBLE PRECISION NOT NULL,
    expected_ctc DOUBLE PRECISION NOT NULL,
    official_notice_period DOUBLE PRECISION NOT NULL,
    actual_notice_period DOUBLE PRECISION NOT NULL,
    reason_for_quick_join VARCHAR(255),
    candidacy_status VARCHAR(50) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    candidacy_id BIGINT DEFAULT NULL,
    modified_by_id BIGINT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    FOREIGN KEY (candidacy_id) REFERENCES candidacies(id) ON DELETE CASCADE,
    FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
);

--rollback DROP TABLE IF EXISTS candidacy_history;
--rollback DROP TRIGGER IF EXISTS update_candidacies_updated_at ON candidacies;
--rollback DROP TABLE IF EXISTS candidacies;
