# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cart_details (
  id                        bigserial not null,
  customer_id               bigint,
  products_id               bigint,
  amount                    float,
  quantity                  bigint,
  status                    varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_cart_details primary key (id))
;

create table category (
  id                        bigserial not null,
  category_name             varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_category primary key (id))
;

create table customer (
  id                        bigserial not null,
  customer_name             varchar(255),
  email                     varchar(255),
  password                  varchar(255),
  gender                    varchar(255),
  address                   varchar(255),
  mobile                    bigint,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_customer primary key (id))
;

create table menu (
  id                        bigserial not null,
  menu_name                 varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_menu primary key (id))
;

create table products (
  id                        bigserial not null,
  category_id               bigint not null,
  brand                     varchar(255),
  color                     varchar(255),
  price                     float,
  waranty                   varchar(255),
  description               varchar(255),
  image                     bytea,
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint pk_products primary key (id))
;

create table role (
  id                        bigserial not null,
  role                      varchar(255),
  created_on                timestamp not null,
  last_update               timestamp not null,
  constraint uq_role_role unique (role),
  constraint pk_role primary key (id))
;


create table Customer_Role (
  customer_id                    bigint not null,
  role_id                        bigint not null,
  constraint pk_Customer_Role primary key (customer_id, role_id))
;
alter table cart_details add constraint fk_cart_details_customer_1 foreign key (customer_id) references customer (id);
create index ix_cart_details_customer_1 on cart_details (customer_id);
alter table cart_details add constraint fk_cart_details_products_2 foreign key (products_id) references products (id);
create index ix_cart_details_products_2 on cart_details (products_id);
alter table products add constraint fk_products_category_3 foreign key (category_id) references category (id);
create index ix_products_category_3 on products (category_id);



alter table Customer_Role add constraint fk_Customer_Role_customer_01 foreign key (customer_id) references customer (id);

alter table Customer_Role add constraint fk_Customer_Role_role_02 foreign key (role_id) references role (id);

# --- !Downs

drop table if exists cart_details cascade;

drop table if exists category cascade;

drop table if exists customer cascade;

drop table if exists Customer_Role cascade;

drop table if exists menu cascade;

drop table if exists products cascade;

drop table if exists role cascade;

