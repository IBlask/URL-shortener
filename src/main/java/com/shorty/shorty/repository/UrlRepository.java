package com.shorty.shorty.repository;

import com.shorty.shorty.entity.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository <Url, Integer> {

    Url findFirstByOrderByUrlIdDesc();

    Url findByShortUrlId(String shortUrlId);

    Url findByFullUrl(String fullUrl);

    @Transactional
    int deleteByShortUrlId(String shortUrlId);
}
