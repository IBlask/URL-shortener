package com.shorty.shorty.repository;

import com.shorty.shorty.entity.Url;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlRepository extends JpaRepository <Url, Integer> {

    Url findFirstByOrderByUrlIdDesc();

    Url findByShortUrlId(String shortUrlId);

    @Transactional
    int deleteByShortUrlId(String shortUrlId);
}
