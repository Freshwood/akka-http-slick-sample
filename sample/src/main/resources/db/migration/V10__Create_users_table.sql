CREATE TABLE users (
  id          UUID PRIMARY KEY,
  login       VARCHAR  NOT NULL,
  pwdhash     VARCHAR NOT NULL,
  email       VARCHAR NOT NULL,
  firstname   VARCHAR,
  lastname    VARCHAR,
  read_only   BOOLEAN NOT NULL DEFAULT false,
  created_at  TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at  TIMESTAMP,
  deleted_at  TIMESTAMP
);


CREATE UNIQUE INDEX users_login ON users (login);
