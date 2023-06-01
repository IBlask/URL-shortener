package com.shorty.shorty.repository;

import com.shorty.shorty.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlRepository extends JpaRepository <Url, Integer> {

    List<Url> findAllByUserId(int userId);
}
