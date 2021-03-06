CREATE TABLE sys_user (
                       id serial PRIMARY KEY,
                       username VARCHAR ( 50 ) UNIQUE NOT NULL,
                       encrypted_password VARCHAR ( 100 ) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP NOT NULL DEFAULT now(),
                       status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO sys_user(id, username, encrypted_password)values(1, 'Student1', '123456');
INSERT INTO sys_user(id, username, encrypted_password)values(2, 'Teacher2', '123456');
INSERT INTO sys_user(id, username, encrypted_password)values(3, 'Admin3', '123456');

alter sequence sys_user_id_seq restart with 4;


CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT now(),
                      updated_at TIMESTAMP NOT NULL DEFAULT now(),
                      status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO role(id, name)values(1, 'student');
INSERT INTO role(id,name)values(2, 'teacher');
INSERT INTO role(id,name)values(3, 'admin');
alter sequence role_id_seq restart with 4;

CREATE TABLE user_role(
                          id SERIAL PRIMARY KEY,
                          user_id INTEGER NOT NULL,
                          role_id INTEGER NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT now(),
                          updated_at TIMESTAMP NOT NULL DEFAULT now(),
                          status VARCHAR(10) NOT NULL DEFAULT 'OK'
);


INSERT INTO user_role(user_id, role_id) VALUES(1,1);
INSERT INTO user_role(user_id, role_id) VALUES(2,2);
INSERT INTO user_role(user_id, role_id) VALUES(3,3);
alter sequence user_role_id_seq restart with 4;

CREATE TABLE permission(
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(50) NOT NULL,
                           role_id INTEGER NOT NULL,
                           created_at TIMESTAMP NOT NULL DEFAULT now(),
                           updated_at TIMESTAMP NOT NULL DEFAULT now(),
                           status VARCHAR(10) NOT NULL DEFAULT 'OK'
);

INSERT INTO permission(name, role_id) values('????????????', 1);
INSERT INTO permission(name, role_id) values('????????????', 2);
INSERT INTO permission(name, role_id) values('????????????', 3);
INSERT INTO permission(name, role_id) values('????????????', 2);
INSERT INTO permission(name, role_id) values('????????????', 3);
INSERT INTO permission(name, role_id) values('????????????', 3);