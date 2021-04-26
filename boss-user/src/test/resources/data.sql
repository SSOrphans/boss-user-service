-- insert into role(name)
-- values("role name");

-- insert into user_type(name, authority)
-- values ("type name", 1);

insert into user(display_name, email, password, created, deleted, confirmed)
values ('Jane Smith', 'janesmith@ss.com', '1234', '2077-01-01', null, 1);

insert into user(display_name, email, password, created, deleted, confirmed)
values ('Joe Smith', 'joesmith@ss.com', '1234', '2077-01-01', '2077-01-02', 1);

insert into user(display_name, email, password, created, deleted, confirmed)
values ('Jack Smith', 'jacksmith@ss.com', '1234', '2077-01-01', null, 1);