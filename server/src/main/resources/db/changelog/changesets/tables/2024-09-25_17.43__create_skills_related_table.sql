--liquibase formatted sql
--changeset lucavassos:create_skills_related_table splitStatements:true

CREATE TABLE skills (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TRIGGER update_skills_updated_at
BEFORE UPDATE ON skills
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TABLE skills_history (
    event_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    skill_id BIGINT,
    modified_by_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    CONSTRAINT fk_skill_history_skill FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    CONSTRAINT fk_skill_history_modified_by FOREIGN KEY (modified_by_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE jobs_skills (
    jobs_id BIGINT NOT NULL,
    skills_id BIGINT NOT NULL,

    PRIMARY KEY (jobs_id, skills_id),
    CONSTRAINT fk_job FOREIGN KEY (jobs_id) REFERENCES jobs(id) ON DELETE CASCADE,
    CONSTRAINT fk_skill FOREIGN KEY (skills_id) REFERENCES skills(id) ON DELETE CASCADE
);

--rollback DROP TABLE IF EXISTS skills_history;
--rollback DROP TABLE IF EXISTS job_skills;
--rollback DROP TRIGGER IF EXISTS update_skills_updated_at ON skills;
--rollback DROP TABLE IF EXISTS skills;
