--liquibase formatted sql
--changeset lucavassos:bootstrap_clients_table splitStatements:true

INSERT INTO clients (name, industry)
VALUES
    ('TCS', 'CONSULTANCY');
