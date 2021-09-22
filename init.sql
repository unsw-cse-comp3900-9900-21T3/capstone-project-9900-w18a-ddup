create table ecomm_user
(
    id                  bigint  not null
        constraint ecomm_user_pkey
            primary key,
    created_date        timestamp,
    last_modified_date  timestamp,
    email               varchar(255)
        constraint uk_ak6dtts5ta8xvrpo5bh2fanhq
            unique,
    enabled             boolean not null,
    first_name          varchar(255),
    last_name           varchar(255),
    password            varchar(255),
    created_by_id       bigint
        constraint fkc8v599lr6915bg8ubgg5vo9op
            references ecomm_user,
    last_modified_by_id bigint
        constraint fkecnsgeetoumk11bueosr1awv7
            references ecomm_user
);

alter table ecomm_user
    owner to "e-comm";

create table item
(
    id                  bigint not null
        constraint item_pkey
            primary key,
    created_date        timestamp,
    last_modified_date  timestamp,
    name                varchar(255)
        constraint uk_lcsp6a1tpwb8tfywqhrsm2uvg
            unique,
    price               double precision,
    created_by_id       bigint
        constraint fkl6stqg2fjr7y2ddn3tqqb0dvy
            references item,
    last_modified_by_id bigint
        constraint fkn6pymind4hfekgclskad7hgcd
            references item
);

alter table item
    owner to "e-comm";

create table privilege
(
    id                  bigint not null
        constraint privilege_pkey
            primary key,
    created_date        timestamp,
    last_modified_date  timestamp,
    name                varchar(255),
    created_by_id       bigint
        constraint fk8tbgw842jdee72b3rlloe1q7x
            references privilege,
    last_modified_by_id bigint
        constraint fkm9c3j76wqia3abpgyn4o1apff
            references privilege
);

alter table privilege
    owner to "e-comm";

create table role
(
    id                  bigint not null
        constraint role_pkey
            primary key,
    created_date        timestamp,
    last_modified_date  timestamp,
    name                varchar(255),
    created_by_id       bigint
        constraint fk5gkbg0firnt6ltcy68w86ii99
            references role,
    last_modified_by_id bigint
        constraint fkgy6beah27bsbi6sxkvdkr19si
            references role
);

alter table role
    owner to "e-comm";

create table roles_privileges
(
    role_id      bigint not null
        constraint fk9h2vewsqh8luhfq71xokh4who
            references role,
    privilege_id bigint not null
        constraint fk5yjwxw2gvfyu76j3rgqwo685u
            references privilege
);

alter table roles_privileges
    owner to "e-comm";

create table users_roles
(
    user_id bigint not null
        constraint fkf592t5xyo8ev1fua75ug6q7jd
            references ecomm_user,
    role_id bigint not null
        constraint fkt4v0rrweyk393bdgt107vdx0x
            references role
);

alter table users_roles
    owner to "e-comm";


