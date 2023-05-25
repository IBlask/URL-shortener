package com.shorty.shorty.repository;

import com.shorty.shorty.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository <Url, Integer> {

    @Query(value = "SELECT * FROM URL_TABLE WHERE user_id = :userId", nativeQuery = true)
    List<Url> selectStatistics(@Param("userId") int userId);
}
