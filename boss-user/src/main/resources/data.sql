--USER
insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'gta5', 'gta5@ss.com', '$2y$10$PRt9o6hX8D3uGujy85zM5uo9u3sr57BxuxQDVEb/5OfqJEqMY2tBG', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user1', 'user1@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user2', 'user2@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user3', 'user3@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user4', 'user4@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user5', 'user5@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user6', 'user6@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user7', 'user7@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user8', 'user8@ss.com', 'USer!@34', 0, NULL, true, false);

insert into boss.user (type_id, branch_id, username, email, password, created, deleted, enabled, locked)
values (2, 1, 'user9', 'user9@ss.com', 'USer!@34', 0, NULL, true, false);

--ACCOUNT HOLDER
insert into boss.account_holder(user_id, full_name, dob, ssn, address, city, state, zip, phone)
values(1, 'Trevor Philips', 20130917, '123-45-6789', '16703 Nicklaus Dr', 'Los Angeles', 'CA', 91342, '+12735550136');