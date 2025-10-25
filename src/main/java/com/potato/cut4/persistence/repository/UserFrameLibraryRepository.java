package com.potato.cut4.persistence.repository;

import com.potato.cut4.persistence.domain.Frame;
import com.potato.cut4.persistence.domain.User;
import com.potato.cut4.persistence.domain.UserFrameLibrary;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFrameLibraryRepository extends JpaRepository<UserFrameLibrary, UUID> {

  Optional<UserFrameLibrary> findByUserAndFrame(User user, Frame frame);

  boolean existsByUserAndFrame(User user, Frame frame);

  Page<UserFrameLibrary> findByUser(User user, Pageable pageable);

  Page<UserFrameLibrary> findByUserAndBookmarkedTrue(User user, Pageable pageable);
}
