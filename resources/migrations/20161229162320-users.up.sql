CREATE TABLE boxbot.users (
    user_id         serial not null,
    name            text not null,
    email           text not null,
    password        text not null,
    verify_token    text not null default uuid_generate_v1mc(),
    verified_at     timestamp with time zone,
    created_at      timestamp with time zone not null default now(),

    PRIMARY KEY (user_id)
);
--;;
CREATE UNIQUE INDEX ON boxbot.users (lower(email));
