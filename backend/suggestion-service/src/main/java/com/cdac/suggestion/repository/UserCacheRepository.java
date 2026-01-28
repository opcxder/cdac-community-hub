package com.cdac.suggestion.repository;

import com.cdac.suggestion.model.UserCache;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCacheRepository extends JpaRepository<UserCache, Long> {

    Optional<UserCache> findByUserId(Long userId);
}
