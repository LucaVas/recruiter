--liquibase formatted sql
--changeset lucavassos:create_candidacy_files_related_table splitStatements:true

CREATE TABLE candidacy_files (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    unique_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    candidacy_id BIGINT NOT NULL,

    FOREIGN KEY (candidacy_id) REFERENCES candidacies(id) ON DELETE CASCADE
);

CREATE TRIGGER update_candidacy_files_updated_at
BEFORE UPDATE ON candidacy_files
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();


CREATE TABLE candidacy_files_history (
    event_id UUID NOT NULL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    unique_id CHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    candidacy_file_id BIGINT NOT NULL,
    modified_by_id BIGINT NOT NULL,

    FOREIGN KEY (candidacy_file_id) REFERENCES candidacy_files(id) ON DELETE CASCADE,
    FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
);

