DROP TABLE IF EXISTS users_on_workspaces;
DROP TABLE IF EXISTS users_on_groups;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS workspaces;

CREATE TABLE workspaces
(
    id             VARCHAR(50) PRIMARY KEY,
    workspace_name VARCHAR(50)  NOT NULL,
    description    VARCHAR(1000),
    owner_id       VARCHAR(100) NOT NULL,
    created_on     DATE         NOT NULL
);


CREATE TABLE groups
(
    id          VARCHAR(50) PRIMARY KEY,
    group_name  VARCHAR(50) NOT NULL,
    description VARCHAR(1000),
    created_on  DATE        NOT NULL,
    workspace_id VARCHAR(50) NOT NULL,
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id)
);

CREATE TABLE users_on_workspaces
(
    user_id      VARCHAR(100) NOT NULL,
    workspace_id VARCHAR(50)  NOT NULL,
    is_active    BOOLEAN      NOT NULL,
    joined_on    DATE         NOT NULL,
    last_active  TIMESTAMP    NOT NULL,
    PRIMARY KEY (user_id, workspace_id),
    FOREIGN KEY (workspace_id) REFERENCES workspaces (id)
);


CREATE TABLE users_on_groups
(
    group_id  VARCHAR(50),
    user_id   VARCHAR(100),
    joined_on DATE NOT NULL,
    left_at   DATE,
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups (id)
);
