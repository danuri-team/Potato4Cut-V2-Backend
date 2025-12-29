CREATE TABLE creators
(
    id               BINARY(16)    NOT NULL,
    user_id          BINARY(16)    NOT NULL,
    status           VARCHAR(255) NOT NULL,
    rejection_reason VARCHAR(1000) NULL,
    applied_at       datetime     NOT NULL,
    approved_at      datetime NULL,
    updated_at       datetime     NOT NULL,
    CONSTRAINT pk_creators PRIMARY KEY (id)
);

CREATE TABLE follows
(
    id           BINARY(16) NOT NULL,
    follower_id  BINARY(16) NOT NULL,
    following_id BINARY(16) NOT NULL,
    created_at   datetime NOT NULL,
    CONSTRAINT pk_follows PRIMARY KEY (id)
);

CREATE TABLE frame_comments
(
    id             BINARY(16)    NOT NULL,
    user_id        BINARY(16)    NOT NULL,
    frame_id       BINARY(16)    NOT NULL,
    content        VARCHAR(1000) NOT NULL,
    parent_comment BINARY(16)    NULL,
    deleted        BIT(1)        NOT NULL,
    created_at     datetime      NOT NULL,
    updated_at     datetime      NOT NULL,
    CONSTRAINT pk_frame_comments PRIMARY KEY (id)
);

CREATE TABLE frame_likes
(
    id         BINARY(16) NOT NULL,
    user_id    BINARY(16) NOT NULL,
    frame_id   BINARY(16) NOT NULL,
    created_at datetime NOT NULL,
    CONSTRAINT pk_frame_likes PRIMARY KEY (id)
);

CREATE TABLE frame_tags
(
    id         BINARY(16) NOT NULL,
    frame_id   BINARY(16) NOT NULL,
    tag_id     BINARY(16) NOT NULL,
    created_at datetime NOT NULL,
    CONSTRAINT pk_frame_tags PRIMARY KEY (id)
);

CREATE TABLE frames
(
    id                      BINARY(16)    NOT NULL,
    creator_id              BINARY(16)    NOT NULL,
    title                   VARCHAR(100) NOT NULL,
    `description`           VARCHAR(1000) NULL,
    frame_base_image_url    VARCHAR(500) NOT NULL,
    frame_overlay_image_url VARCHAR(500) NOT NULL,
    preview_image_url       VARCHAR(500) NOT NULL,
    category                VARCHAR(255) NOT NULL,
    status                  VARCHAR(255) NOT NULL,
    rejection_reason        VARCHAR(1000) NULL,
    download_count          BIGINT       NOT NULL,
    like_count              BIGINT       NOT NULL,
    view_count              BIGINT       NOT NULL,
    price                   INT          NOT NULL,
    is_public               BIT(1)       NOT NULL,
    created_at              datetime     NOT NULL,
    updated_at              datetime     NOT NULL,
    approved_at             datetime NULL,
    CONSTRAINT pk_frames PRIMARY KEY (id)
);

CREATE TABLE notifications
(
    id         BINARY(16)   NOT NULL,
    user_id    BINARY(16)   NOT NULL,
    actor_id   BINARY(16)   NULL,
    type       VARCHAR(255) NOT NULL,
    content    VARCHAR(500) NOT NULL,
    target_url VARCHAR(500) NULL,
    is_read    BIT(1)       NOT NULL,
    created_at datetime     NOT NULL,
    read_at    datetime NULL,
    CONSTRAINT pk_notifications PRIMARY KEY (id)
);

CREATE TABLE photos
(
    id                 BINARY(16)   NOT NULL,
    user_id            BINARY(16)   NOT NULL,
    frame_id           BINARY(16)   NULL,
    composed_image_url VARCHAR(500) NOT NULL,
    deleted            BIT(1)       NOT NULL,
    created_at         datetime     NOT NULL,
    CONSTRAINT pk_photos PRIMARY KEY (id)
);

CREATE TABLE profile_preset
(
    id      BINARY(16)   NOT NULL,
    title   VARCHAR(50)  NOT NULL,
    img_url VARCHAR(100) NOT NULL,
    CONSTRAINT pk_profile_preset PRIMARY KEY (id)
);

CREATE TABLE reports
(
    id           BINARY(16)    NOT NULL,
    reporter_id  BINARY(16)    NOT NULL,
    type         VARCHAR(255)  NOT NULL,
    target_id    BINARY(16)    NULL,
    reason       VARCHAR(1000) NOT NULL,
    status       VARCHAR(255)  NOT NULL,
    admin_note   VARCHAR(1000) NULL,
    processed_by BINARY(16)    NULL,
    created_at   datetime      NOT NULL,
    updated_at   datetime      NOT NULL,
    processed_at datetime NULL,
    CONSTRAINT pk_reports PRIMARY KEY (id)
);

CREATE TABLE shares
(
    id        BINARY(16)   NOT NULL,
    code      VARCHAR(10)  NOT NULL,
    photo_id  BINARY(16)   NOT NULL,
    type      VARCHAR(255) NOT NULL,
    expire_at datetime NULL,
    CONSTRAINT pk_shares PRIMARY KEY (id)
);

CREATE TABLE tags
(
    id          BINARY(16)  NOT NULL,
    name        VARCHAR(50) NOT NULL,
    usage_count BIGINT      NOT NULL,
    CONSTRAINT pk_tags PRIMARY KEY (id)
);

CREATE TABLE user_devices
(
    id         BINARY(16)   NOT NULL,
    user_id    BINARY(16)   NOT NULL,
    fcm_token  VARCHAR(500) NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    CONSTRAINT pk_user_devices PRIMARY KEY (id)
);

CREATE TABLE user_frame_libraries
(
    id           BINARY(16) NOT NULL,
    user_id      BINARY(16) NOT NULL,
    frame_id     BINARY(16) NOT NULL,
    bookmarked   BIT(1)   NOT NULL,
    added_at     datetime NOT NULL,
    last_used_at datetime NULL,
    CONSTRAINT pk_user_frame_libraries PRIMARY KEY (id)
);

CREATE TABLE users
(
    id                   BINARY(16)   NOT NULL,
    nickname             VARCHAR(50)  NOT NULL,
    profile_image_url    VARCHAR(500) NULL,
    bio                  VARCHAR(500) NULL,
    `role`               VARCHAR(255) NOT NULL,
    social_provider      VARCHAR(255) NULL,
    social_id            VARCHAR(255) NULL,
    email                VARCHAR(100) NULL,
    deleted              BIT(1)       NOT NULL,
    notification_enabled BIT(1)       NOT NULL,
    created_at           datetime     NOT NULL,
    updated_at           datetime     NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE creators
    ADD CONSTRAINT uc_creators_user UNIQUE (user_id);

ALTER TABLE shares
    ADD CONSTRAINT uc_shares_code UNIQUE (code);

ALTER TABLE shares
    ADD CONSTRAINT uc_shares_photo UNIQUE (photo_id);

ALTER TABLE tags
    ADD CONSTRAINT uc_tags_name UNIQUE (name);

ALTER TABLE user_devices
    ADD CONSTRAINT uc_user_devices_fcmtoken UNIQUE (fcm_token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_users_nickname UNIQUE (nickname);

ALTER TABLE users
    ADD CONSTRAINT uc_users_socialid UNIQUE (social_id);

ALTER TABLE creators
    ADD CONSTRAINT FK_CREATORS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE follows
    ADD CONSTRAINT FK_FOLLOWS_ON_FOLLOWER FOREIGN KEY (follower_id) REFERENCES users (id);

ALTER TABLE follows
    ADD CONSTRAINT FK_FOLLOWS_ON_FOLLOWING FOREIGN KEY (following_id) REFERENCES users (id);

ALTER TABLE frames
    ADD CONSTRAINT FK_FRAMES_ON_CREATOR FOREIGN KEY (creator_id) REFERENCES creators (id);

ALTER TABLE frame_comments
    ADD CONSTRAINT FK_FRAME_COMMENTS_ON_FRAME FOREIGN KEY (frame_id) REFERENCES frames (id);

ALTER TABLE frame_comments
    ADD CONSTRAINT FK_FRAME_COMMENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE frame_likes
    ADD CONSTRAINT FK_FRAME_LIKES_ON_FRAME FOREIGN KEY (frame_id) REFERENCES frames (id);

ALTER TABLE frame_likes
    ADD CONSTRAINT FK_FRAME_LIKES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE frame_tags
    ADD CONSTRAINT FK_FRAME_TAGS_ON_FRAME FOREIGN KEY (frame_id) REFERENCES frames (id);

ALTER TABLE frame_tags
    ADD CONSTRAINT FK_FRAME_TAGS_ON_TAG FOREIGN KEY (tag_id) REFERENCES tags (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_ACTOR FOREIGN KEY (actor_id) REFERENCES users (id);

ALTER TABLE notifications
    ADD CONSTRAINT FK_NOTIFICATIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE photos
    ADD CONSTRAINT FK_PHOTOS_ON_FRAME FOREIGN KEY (frame_id) REFERENCES frames (id);

ALTER TABLE photos
    ADD CONSTRAINT FK_PHOTOS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE reports
    ADD CONSTRAINT FK_REPORTS_ON_PROCESSED_BY FOREIGN KEY (processed_by) REFERENCES users (id);

ALTER TABLE reports
    ADD CONSTRAINT FK_REPORTS_ON_REPORTER FOREIGN KEY (reporter_id) REFERENCES users (id);

ALTER TABLE shares
    ADD CONSTRAINT FK_SHARES_ON_PHOTO FOREIGN KEY (photo_id) REFERENCES photos (id);

ALTER TABLE user_devices
    ADD CONSTRAINT FK_USER_DEVICES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE user_frame_libraries
    ADD CONSTRAINT FK_USER_FRAME_LIBRARIES_ON_FRAME FOREIGN KEY (frame_id) REFERENCES frames (id);

ALTER TABLE user_frame_libraries
    ADD CONSTRAINT FK_USER_FRAME_LIBRARIES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);