package com.example.gitmanager.notice.repository;

import com.example.gitmanager.notice.entity.NoticeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCategoryRepository extends JpaRepository<NoticeCategory, Long> {
    NoticeCategory findByName(String name);
}
