CREATE TABLE boxbot.boxes (
    box_id          serial not null,
    user_id         integer not null references boxbot.users (user_id) on update cascade on delete cascade,
    location_id     integer references boxbot.locations (location_id) on update cascade on delete cascade,
    name            text not null,
    description     text,
    created_at      timestamp with time zone not null default now(),

    PRIMARY KEY (box_id)
);
--;;
CREATE UNIQUE INDEX ON boxbot.boxes (user_id, lower(name));
--;;
CREATE INDEX ON boxbot.boxes (location_id);
