package com.interview.urlshortner.repository;

import com.interview.urlshortner.entity.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<UrlEntity, Long> {
    Optional<UrlEntity> findByLongUrl(String fullUrl);

    Optional<UrlEntity> findByShortUrl(String fullUrl);
}
