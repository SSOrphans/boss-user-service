create schema if not exists boss;
create table if not exists boss.user (
   id INT NOT NULL AUTO_INCREMENT UNIQUE,
   type_id TINYINT NOT NULL,
   branch_id INT NOT NULL,
   username VARCHAR(16) NOT NULL UNIQUE,
   email VARCHAR(128) NOT NULL UNIQUE,
   password CHAR(64) NOT NULL,
   created BIGINT NOT NULL,
   deleted BIGINT,
   enabled BIT NOT NULL,
   locked BIT NOT NULL,
   PRIMARY KEY (id)
);
