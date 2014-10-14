# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table contact (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  surname                   varchar(255),
  email                     varchar(255),
  company_name              varchar(255),
  user_id                   bigint,
  constraint pk_contact primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  password                  varchar(255),
  is_admin                  boolean not null,
  photo                     varchar(255),
  constraint uq_user_name unique (name),
  constraint pk_user primary key (id))
;

alter table contact add constraint fk_contact_user_1 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_contact_user_1 on contact (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists contact;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

