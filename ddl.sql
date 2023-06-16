DROP SCHEMA PUBLIC CASCADE;
CREATE SCHEMA PUBLIC;

CREATE TABLE account (
  id BIGSERIAL PRIMARY KEY,
  license VARCHAR(36) UNIQUE NOT NULL,
  email_address VARCHAR UNIQUE NOT NULL,
  pin VARCHAR(7) NOT NULL,
  activated BIGINT NOT NULL,
  deactivated BIGINT NOT NULL
);

CREATE TABLE fault (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT REFERENCES account(id),
  cause VARCHAR NOT NULL,
  occurred BIGINT NOT NULL
);

CREATE TABLE profile (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT REFERENCES account(id),
  name VARCHAR NOT NULL,
  created BIGINT NOT NULL
);

CREATE TABLE edible (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES profile(id),
  kind VARCHAR NOT NULL,
  detail VARCHAR NOT NULL,
  organic BOOL NOT NULL,
  calories INT NOT NULL,
  ate BIGINT NOT NULL
);

CREATE TABLE drinkable (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES profile(id),
  kind VARCHAR NOT NULL,
  detail VARCHAR NOT NULL,
  organic BOOL NOT NULL,
  count INT NOT NULL,
  calories INT NOT NULL,
  drank BIGINT NOT NULL
);

CREATE TABLE expendable (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES profile(id),
  kind VARCHAR NOT NULL,
  detail VARCHAR NOT NULL,
  sunshine BOOL NOT NULL,
  freshair BOOL NOT NULL,
  calories INT NOT NULL,
  start BIGINT NOT NULL,
  finish BIGINT NOT NULL
);

CREATE TABLE measurable (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES profile(id),
  measurement INT NOT NULL,
  unit VARCHAR NOT NULL,
  measured BIGINT NOT NULL
);