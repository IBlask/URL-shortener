package com.shorty.shorty.repository;

import com.shorty.shorty.entity.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository <Url, Integer> {
    @Query(value = "SELECT short_url_id FROM URL_TABLE WHERE url_id=(SELECT max(url_id) FROM URL_TABLE);", nativeQuery = true)
    String findLastShortUrlId();

    @Query(value = "SELECT CASE WHEN EXISTS (SELECT 1 FROM URL_TABLE WHERE short_url_id = :shortUrlId) THEN CAST(1 AS BIT) ELSE CAST(0 AS BIT) END", nativeQuery = true)
    boolean shortUrlIdExistsInDatabase(@Param("shortUrlId") String shortUrlId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM URL_TABLE WHERE short_url_id = :shortUrlId", nativeQuery = true)
    int deleteUrlByShortUrlId(@Param("shortUrlId") String shortUrlId);
}
