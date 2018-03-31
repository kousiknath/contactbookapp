CREATE TABLE contact (
id SERIAL PRIMARY KEY,
name varchar(100) NULL,
email varchar(100) UNIQUE NOT NULL
);
