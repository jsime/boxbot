CREATE TABLE boxbot.items (
    item_id         serial not null,
    user_id         integer not null references boxbot.users (user_id) on update cascade on delete cascade,
    box_id          integer references boxbot.boxes (box_id) on update cascade on delete cascade,
    name            text not null,
    description     text,
    created_at      timestamp with time zone not null default now(),
    boxed_at        timestamp with time zone,

    PRIMARY KEY (item_id)
);
--;;
CREATE INDEX ON boxbot.items (user_id);
--;;
CREATE INDEX ON boxbot.items (box_id);
--;;
CREATE INDEX ON boxbot.items (user_id, box_id);
