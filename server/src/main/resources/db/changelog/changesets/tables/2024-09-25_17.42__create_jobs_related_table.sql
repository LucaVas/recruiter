--liquibase formatted sql
--changeset lucavassos:create_jobs_related_table splitStatements:true


CREATE TABLE jobs (
            id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            status VARCHAR(50) NOT NULL,
            wanted_cvs INT NOT NULL CHECK (wanted_cvs >= 0),
            contract_type VARCHAR(50) NOT NULL,
            experience_range_min INT NOT NULL CHECK (experience_range_min >= 0),
            experience_range_max INT NOT NULL CHECK (experience_range_max <= 60),
            notice_period_in_days INT NOT NULL CHECK (notice_period_in_days >= 0),
            salary_budget DOUBLE PRECISION NOT NULL CHECK (salary_budget >= 0),
            currency VARCHAR(50) NOT NULL,
            description VARCHAR(500) NOT NULL,
            bonus_pay_per_cv DOUBLE PRECISION NOT NULL CHECK (bonus_pay_per_cv >= 0),
            closure_bonus VARCHAR(255) NOT NULL,
            cv_rate_payment_date TIMESTAMP NOT NULL,
            closure_bonus_payment_date TIMESTAMP NOT NULL,
            number_of_candidates INT,
            created_at TIMESTAMP DEFAULT NOW() NOT NULL,
            updated_at TIMESTAMP DEFAULT NOW() NOT NULL,
            recruiter_id BIGINT,
            client_name VARCHAR(255),
            questionnaire_id BIGINT,

            CONSTRAINT fk_job_recruiter FOREIGN KEY (recruiter_id) REFERENCES users(id),
            CONSTRAINT fk_job_client FOREIGN KEY (client_name) REFERENCES clients(name),
            CONSTRAINT fk_job_questionnaire FOREIGN KEY (questionnaire_id) REFERENCES questionnaires(id)
        );

CREATE TABLE jobs_history (
            event_id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
            name VARCHAR(255) NOT NULL,
            status VARCHAR(50) NOT NULL,
            wanted_cvs INT NOT NULL,
            contract_type VARCHAR(50) NOT NULL,
            experience_range_min INT NOT NULL,
            experience_range_max INT NOT NULL,
            notice_period_in_days INT NOT NULL,
            salary_budget DOUBLE PRECISION NOT NULL,
            description VARCHAR(500) NOT NULL,
            bonus_pay_per_cv DOUBLE PRECISION NOT NULL,
            closure_bonus VARCHAR(255) NOT NULL,
            cv_rate_payment_date TIMESTAMP NOT NULL,
            closure_bonus_payment_date TIMESTAMP NOT NULL,
            number_of_candidates INT NOT NULL,
            event_type VARCHAR(50) NOT NULL,
            job_id BIGINT,
            modified_by_id BIGINT,
            created_at TIMESTAMP DEFAULT NOW() NOT NULL,

            CONSTRAINT fk_job_history_job FOREIGN KEY (job_id) REFERENCES jobs(id),
            CONSTRAINT fk_job_history_modified_by FOREIGN KEY (modified_by_id) REFERENCES users(id)
        );
