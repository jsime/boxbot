CREATE TABLE boxbot.locations (
    location_id     serial not null,
    parent_id       integer references boxbot.locations (location_id) on update cascade on delete set null,
    user_id         integer not null references boxbot.users (user_id) on update cascade on delete cascade,
    name            text not null,
    description     text,
    created_at      timestamp with time zone not null default now(),

    PRIMARY KEY(location_id)
);
--;;
CREATE UNIQUE INDEX ON boxbot.locations (user_id, lower(name));
--;;
CREATE INDEX ON boxbot.locations (parent_id);
