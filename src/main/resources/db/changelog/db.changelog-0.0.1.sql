--liquibase formatted sql

--changeset jkmiec:1
CREATE TABLE IF NOT EXISTS exercise (
  id         BIGINT PRIMARY KEY     NOT NULL,
  name       VARCHAR(255)           NOT NULL,
  creator_id BIGINT                 NULL
);

--changeset jkmiec:2
CREATE TABLE IF NOT EXISTS exercise_details (
  id   BIGINT PRIMARY KEY   NOT NULL,
  time INT                  NOT NULL
);

--changeset jkmiec:3
CREATE TABLE IF NOT EXISTS role (
  id   BIGINT PRIMARY KEY   NOT NULL,
  name VARCHAR(255)         NULL
);

--changeset jkmiec:4
CREATE TABLE IF NOT EXISTS user (
  id       BIGINT PRIMARY KEY   NOT NULL,
  password VARCHAR(255)         NULL,
  username VARCHAR(255)         NULL
);

--changeset jkmiec:5
ALTER TABLE exercise
  ADD FOREIGN KEY (creator_id) REFERENCES user (id);

--changeset jkmiec:6
CREATE TABLE IF NOT EXISTS user_role (
  user_id  BIGINT REFERENCES user (id),
  roles_id BIGINT REFERENCES role (id)
);

--changeset jkmiec:7
CREATE TABLE IF NOT EXISTS workout (
  id         BIGINT PRIMARY KEY          NOT NULL,
  name       VARCHAR(255)                NOT NULL,
  sets       INT                         NOT NULL,
  creator_id BIGINT REFERENCES user (id)
);

--changeset jkmiec:8
CREATE TABLE IF NOT EXISTS workout_details (
  id                    BIGINT PRIMARY KEY              NOT NULL,
  exercise_order_number INT                             NOT NULL,
  exercise_id           BIGINT REFERENCES exercise (id),
  workout_id            BIGINT REFERENCES workout (id)
);