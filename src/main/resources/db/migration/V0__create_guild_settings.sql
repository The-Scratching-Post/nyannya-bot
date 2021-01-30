CREATE TABLE guild_settings
(
    id            bigint
        primary key,
    info_channel  bigint,
    join_message  varchar(256),
    leave_message varchar(256)
);
