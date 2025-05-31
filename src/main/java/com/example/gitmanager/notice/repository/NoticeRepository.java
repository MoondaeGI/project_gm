package com.example.gitmanager.notice.repository;

import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.entity.NoticeCategory;
import org.springframework.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @NonNull Page<Notice> findAll(@NonNull Pageable pageable);
    Page<Notice> findByNoticeCategory(@NonNull NoticeCategory noticeCategory, @NonNull Pageable pageable);
    Page<Notice> findByTitleContainingOrContentContaining(
            String title, String content, @NonNull Pageable pageable);
    Page<Notice> findByNoticeCategoryAndTitleContainingOrContentContaining(
            @NonNull NoticeCategory noticeCategory,
            String title, String content, @NonNull Pageable pageable);

    int countBy();
    int countByNoticeCategory(@NonNull NoticeCategory noticeCategory);
    int countByTitleContainingOrContentContaining(String title, String content);
    int countByNoticeCategoryAndTitleContainingOrContentContaining(
            @NonNull NoticeCategory noticeCategory,
            String title, String content);
}
