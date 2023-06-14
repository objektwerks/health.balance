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

CREATE TABLE profile (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES account(id),
  name VARCHAR NOT NULL,
  created BIGINT NOT NULL
);

CREATE TABLE entry (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES profile(id),
  created BIGINT NOT NULL
);

CREATE TABLE edible (
  id BIGSERIAL PRIMARY KEY,
  entry_id BIGINT REFERENCES entry(id),
  kind VARCHAR NOT NULL,
  organic BOOL NOT NULL,
  calories INT NOT NULL,
  ate BIGINT NOT NULL
);

CREATE TABLE drinkable (
  id BIGSERIAL PRIMARY KEY,
  entry_id BIGINT REFERENCES entry(id),
  kind VARCHAR NOT NULL,
  organic BOOL NOT NULL,
  calories INT NOT NULL,
  drank BIGINT NOT NULL
);

CREATE TABLE expendable (
  id BIGSERIAL PRIMARY KEY,
  entry_id BIGINT REFERENCES entry(id),
  kind VARCHAR NOT NULL,
  sunshine BOOL NOT NULL,
  freshair BOOL NOT NULL,
  calories INT NOT NULL,
  from BIGINT NOT NULL,
  to BIGINT NOT NULL
);

CREATE TABLE measurable (
  id BIGSERIAL PRIMARY KEY,
  entry_id BIGINT REFERENCES entry(id),
  measurement INT NOT NULL,
  measured BIGINT NOT NULL
);

CREATE TABLE fault (
  id BIGSERIAL PRIMARY KEY,
  profile_id BIGINT REFERENCES account(id),
  cause VARCHAR NOT NULL,
  occurred BIGINT NOT NULL
);