create table if not exists discord_guild
(
    id                 varchar(255) not null
        primary key,
    have_basic_role    boolean default false,
    have_leave_timer   boolean,
    message_id         varchar(255),
    have_log_guild     boolean default false,
    have_log_member    boolean default false,
    log_guild_channel  varchar(255),
    log_member_channel varchar(255)
);

create table if not exists discord_role
(
    id       varchar(255) not null
        primary key,
    reaction varchar(255),
    vip      boolean      not null,
    guild_id varchar(255)
        constraint fk9ue2y3y24nb66nav0dkaoit41
            references discord_guild,
    basic    boolean default false
);

create table if not exists discord_user
(
    id       varchar(255) not null
        primary key,
    username varchar(255)
);

create table if not exists guild_member
(
    id              uuid                       not null
        primary key,
    is_leave_timer  boolean,
    last_out        timestamp(6),
    guild_id        varchar(255)
        constraint fka404442g8a7rf9wg9w9gmkj7r
            references discord_guild,
    member_id       varchar(255)
        constraint fklys7inv9adca0daw98ckt0le
            references discord_user,
    level           integer          default 1 not null,
    voice_time      double precision default 0.0,
    last_voice_time timestamp(6),
    xp              bigint           default 1 not null,
    constraint uksc0hv6p4fmgayuj85lur033ml
        unique (member_id, guild_id)
);

create table if not exists user_role
(
    id      uuid         not null
        primary key,
    role_id varchar(255) not null
        constraint fkkhl6iiqlj7vp7d4vpueqmv1im
            references discord_role,
    user_id varchar(255) not null
        constraint fkjgdy4qxl4hvasb7pt7811b1kt
            references discord_user,
    constraint uk872xec3woupu3gw59b04pj3sa
        unique (role_id, user_id)
);

