-- =====================================================
-- 네컷 이미지 URL 필드 이름 변경
-- =====================================================

ALTER TABLE photos
    CHANGE COLUMN composed_image_url image_url VARCHAR(500) NOT NULL;