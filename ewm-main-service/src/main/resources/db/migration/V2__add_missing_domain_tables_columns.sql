ALTER TABLE events
    ADD COLUMN IF NOT EXISTS confirmed_requests INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN IF NOT EXISTS views BIGINT NOT NULL DEFAULT 0;

CREATE TABLE IF NOT EXISTS "userRequests" (
    id BIGSERIAL PRIMARY KEY,
    created TIMESTAMP,
    event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
    requester_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    status VARCHAR(20),
    CONSTRAINT uk_user_requests_event_requester UNIQUE (event_id, requester_id)
);

CREATE INDEX IF NOT EXISTS idx_user_requests_event_id ON "userRequests" (event_id);
CREATE INDEX IF NOT EXISTS idx_user_requests_requester_id ON "userRequests" (requester_id);

CREATE TABLE IF NOT EXISTS comments (
    id BIGSERIAL PRIMARY KEY,
    text VARCHAR(255) NOT NULL,
    author_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    created TIMESTAMP NOT NULL,
    edited TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_comments_event_id ON comments (event_id);
CREATE INDEX IF NOT EXISTS idx_comments_author_id ON comments (author_id);

CREATE TABLE IF NOT EXISTS comment_likes (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    comment_id BIGINT NOT NULL REFERENCES comments(id) ON DELETE CASCADE,
    CONSTRAINT uk_comment_likes_user_comment UNIQUE (user_id, comment_id)
);

CREATE INDEX IF NOT EXISTS idx_comment_likes_comment_id ON comment_likes (comment_id);
