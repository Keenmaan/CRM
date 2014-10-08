# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table company (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  email                     varchar(255),
  phone                     varchar(255),
  contact_id                bigint,
  constraint pk_company primary key (id))
;

create table contact (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  surname                   varchar(255),
  email                     varchar(255),
  photo_id                  varchar(255),
  user_id                   bigint,
  constraint pk_contact primary key (id))
;

create table user (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  password                  varchar(255),
  constraint pk_user primary key (id))
;

alter table company add constraint fk_company_contact_1 foreign key (contact_id) references contact (id) on delete restrict on update restrict;
create index ix_company_contact_1 on company (contact_id);
alter table contact add constraint fk_contact_user_2 foreign key (user_id) references user (id) on delete restrict on update restrict;
create index ix_contact_user_2 on contact (user_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists company;

drop table if exists contact;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;
