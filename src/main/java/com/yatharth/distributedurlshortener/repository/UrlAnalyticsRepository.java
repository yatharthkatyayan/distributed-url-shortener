package com.yatharth.distributedurlshortener.repository;

import com.yatharth.distributedurlshortener.entity.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlAnalyticsRepository
        extends JpaRepository<UrlAnalytics, Long> {

    boolean existsById(Long id);
}