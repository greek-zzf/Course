CREATE TABLE sys_user(
    id serial PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    encrypted_password VARCHAR (50) NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT now(),
    updated_at TIMESTAMP  NOT NULL DEFAULT now(),
    status VARCHAR (10) NOT NULL DEFAULT 'OK'
);

CREATE TABLE role(
    id serial PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP  NOT NULL DEFAULT now(),
    updated_at TIMESTAMP  NOT NULL DEFAULT now(),
    status VARCHAR (10) NOT NULL DEFAULT 'OK'
);

INSERT INTO role(id, name)values(1, '学生');
INSERT INTO role(id, name)values(2, '老师');
INSERT INTO role(id, name)values(3, '管理员');