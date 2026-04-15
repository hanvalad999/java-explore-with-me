CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(250) NOT NULL,
    email VARCHAR(254) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    description VARCHAR(7000) NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    initiator_id BIGINT NOT NULL REFERENCES users(id),
    category_id BIGINT NOT NULL REFERENCES categories(id),
    state VARCHAR(20) NOT NULL,
    event_date TIMESTAMP NOT NULL,
    created_on TIMESTAMP NOT NULL,
    published_on TIMESTAMP,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    request_moderation BOOLEAN NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_events_event_date ON events (event_date);
CREATE INDEX IF NOT EXISTS idx_events_category_id ON events (category_id);
CREATE INDEX IF NOT EXISTS idx_events_initiator_id ON events (initiator_id);
CREATE INDEX IF NOT EXISTS idx_events_state ON events (state);

CREATE TABLE IF NOT EXISTS participation_requests (
    id BIGSERIAL PRIMARY KEY,
    requester_id BIGINT NOT NULL REFERENCES users(id),
    event_id BIGINT NOT NULL REFERENCES events(id),
    status VARCHAR(20) NOT NULL,
    created TIMESTAMP NOT NULL,
    CONSTRAINT uk_requester_event UNIQUE (requester_id, event_id)
);

CREATE INDEX IF NOT EXISTS idx_requests_event_id ON participation_requests (event_id);
CREATE INDEX IF NOT EXISTS idx_requests_requester_id ON participation_requests (requester_id);
CREATE INDEX IF NOT EXISTS idx_requests_status ON participation_requests (status);

CREATE TABLE IF NOT EXISTS compilations (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    pinned BOOLEAN NOT NULL
);

CREATE TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    PRIMARY KEY (compilation_id, event_id)
);

CREATE INDEX IF NOT EXISTS idx_compilation_events_event_id ON compilation_events (event_id);
