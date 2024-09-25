--liquibase formatted sql
--changeset lucavassos:create_candidacy_comments_table splitStatements:true

CREATE TABLE candidacy_comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    candidacy_id BIGINT,
    author_id BIGINT,

    FOREIGN KEY (candidacy_id) REFERENCES candidacies(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER update_candidacy_comments_updated_at
BEFORE UPDATE ON candidacy_comments
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

--rollback DROP TRIGGER IF EXISTS update_candidacy_comments_updated_at ON candidacy_comments;
--rollback DROP TABLE IF EXISTS candidacy_comments;
