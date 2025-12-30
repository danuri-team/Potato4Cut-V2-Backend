-- =====================================================
-- 기본 데이터 삽입
-- =====================================================

-- 시스템 관리자 사용자 생성 (기본 프레임 소유자)
INSERT INTO users (id, nickname, profile_image_url, bio, `role`, social_provider, social_id, email,
                   deleted, notification_enabled, created_at, updated_at)
VALUES (UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
        '4CUT 공식',
        NULL,
        '4CUT 공식 계정입니다.',
        'ADMIN',
        NULL,
        NULL,
        'official@4cut.com',
        0,
        0,
        NOW(),
        NOW());

-- 시스템 관리자를 크리에이터로 등록
INSERT INTO creators (id, user_id, status, rejection_reason, applied_at, approved_at, updated_at)
VALUES (UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
        UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
        'APPROVED',
        NULL,
        NOW(),
        NOW(),
        NOW());

-- =====================================================
-- 기본 프레임 데이터 삽입
-- =====================================================

INSERT INTO frames (id, creator_id, title, `description`, frame_base_image_url,
                    frame_overlay_image_url, preview_image_url, category, status, rejection_reason,
                    download_count, like_count, view_count, price, is_public, created_at,
                    updated_at, approved_at)
VALUES (UNHEX(REPLACE('10000000-0000-0000-0000-000000000001', '-', '')),
        UNHEX(REPLACE('00000000-0000-0000-0000-000000000001', '-', '')),
        '감자네컷_기본프레임',
        '감자네컷 공식 기본 프레임입니다.',
        'https://storage.danuri.cloud/frames/DEFAULT-BASE-FRAME.png',
        'https://storage.danuri.cloud/frames/DEFAULT-OVERLAY-FRAME.png',
        'https://storage.danuri.cloud/frames/DEFAULT-PREVIEW-FRAME.png',
        'MINIMAL',
        'APPROVED',
        NULL,
        0, 0, 0,
        0,
        1,
        NOW(),
        NOW(),
        NOW());

-- =====================================================
-- 기본 프로필 프리셋 데이터 삽입
-- =====================================================

INSERT INTO profile_preset (id, title, img_url)
VALUES (UUID_TO_BIN(UUID()), '까칠이', 'https://storage.danuri.cloud/profile-presets/표정=까칠이.svg'),
       (UUID_TO_BIN(UUID()), '꼬르륵이', 'https://storage.danuri.cloud/profile-presets/표정=꼬르륵이.svg'),
       (UUID_TO_BIN(UUID()), '달떠엉이', 'https://storage.danuri.cloud/profile-presets/표정=달떠엉이.svg'),
       (UUID_TO_BIN(UUID()), '만족이', 'https://storage.danuri.cloud/profile-presets/표정=만족이.svg'),
       (UUID_TO_BIN(UUID()), '맥진이', 'https://storage.danuri.cloud/profile-presets/표정=맥진이.svg'),
       (UUID_TO_BIN(UUID()), '쌩쌩이', 'https://storage.danuri.cloud/profile-presets/표정=쌩쌩이.svg'),
       (UUID_TO_BIN(UUID()), '엄식이', 'https://storage.danuri.cloud/profile-presets/표정=엄식이.svg'),
       (UUID_TO_BIN(UUID()), '의심이', 'https://storage.danuri.cloud/profile-presets/표정=의심이.svg'),
       (UUID_TO_BIN(UUID()), '지짐이', 'https://storage.danuri.cloud/profile-presets/표정=지짐이.svg'),
       (UUID_TO_BIN(UUID()), '찌글이', 'https://storage.danuri.cloud/profile-presets/표정=찌글이.svg');
