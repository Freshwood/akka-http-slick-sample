CREATE TABLE users (
  id         UUID PRIMARY KEY,
  login      VARCHAR   NOT NULL,
  password    VARCHAR   NOT NULL DEFAULT 'pass',
  email      VARCHAR   NOT NULL,
  first_name  VARCHAR,
  last_name   VARCHAR,
  read_only  BOOLEAN   NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE TABLE roles (
  id         UUID PRIMARY KEY,
  name       VARCHAR   NOT NULL,
  read_only  BOOLEAN   NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
  updated_at TIMESTAMP,
  deleted_at TIMESTAMP
);

CREATE TABLE users_roles (
  user_id UUID NOT NULL,
  role_id UUID NOT NULL
);

CREATE UNIQUE INDEX users_login
  ON users (login);
CREATE UNIQUE INDEX roles_name
  ON roles (name);

ALTER TABLE users_roles
  ADD CONSTRAINT users_roles_fk FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE RESTRICT ON DELETE CASCADE;
ALTER TABLE users_roles
  ADD CONSTRAINT roles_users_fk FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE RESTRICT ON DELETE CASCADE;