INSERT INTO workspaces (id, workspace_name, description, created_on, owner_id)
VALUES ('w1', 'Workspace 1', 'Desc 1', CURRENT_DATE, 'u1'),
       ('w2', 'Workspace 2', 'Desc 2', CURRENT_DATE, 'u4'),
       ('w3', 'Workspace 3', 'Desc 3', CURRENT_DATE, 'u5'),
       ('w4', 'Workspace 4', 'Desc 4', CURRENT_DATE, 'u4'),
       ('w5', 'Workspace 5', 'Desc 5', CURRENT_DATE, 'u6'),
       ('w6', 'Workspace 6', 'Desc 6', CURRENT_DATE, 'u6');

INSERT INTO groups (id, group_name, description, created_on, workspace_id)
VALUES ('g1', 'Group 1', 'Group Desc 1', CURRENT_DATE, 'w1'),
       ('g2', 'Group 2', 'Group Desc 2', CURRENT_DATE, 'w1'),
       ('g3', 'Group 3', 'Group Desc 3', CURRENT_DATE, 'w1'),
       ('g4', 'Group 4', 'Group Desc 4', CURRENT_DATE, 'w2'),
       ('g5', 'Group 5', 'Group Desc 5', CURRENT_DATE, 'w2'),
       ('g6', 'Group 6', 'Group Desc 6', CURRENT_DATE, 'w3');


INSERT INTO users_on_workspaces
    (user_id, workspace_id, joined_on, last_active, is_active)
VALUES
-- w1
('u1', 'w1', CURRENT_DATE, NOW(), true),
('u2', 'w1', CURRENT_DATE, NOW(), true),
('u3', 'w1', CURRENT_DATE, NOW(), false),

-- w2
('u2', 'w2', CURRENT_DATE, NOW(), true),
('u4', 'w2', CURRENT_DATE, NOW(), true),

-- w3
('u1', 'w3', CURRENT_DATE, NOW(), true),
('u5', 'w3', CURRENT_DATE, NOW(), true),

-- w4
('u4', 'w4', CURRENT_DATE, NOW(), true),
('u1', 'w4', CURRENT_DATE, NOW(), true),

-- w5
('u5', 'w5', CURRENT_DATE, NOW(), true),
('u6', 'w5', CURRENT_DATE, NOW(), true),

-- w6
('u6', 'w6', CURRENT_DATE, NOW(), true);



INSERT INTO users_on_groups
    (group_id, user_id, joined_on, left_at)
VALUES ('g1', 'u1', CURRENT_DATE, NULL),
       ('g1', 'u2', CURRENT_DATE, NULL),
       ('g2', 'u1', CURRENT_DATE, NULL),
       ('g3', 'u3', CURRENT_DATE, NULL),
       ('g4', 'u2', CURRENT_DATE, NULL),
       ('g4', 'u4', CURRENT_DATE, NULL);