package com.example.gitmanager.notice.repository;

import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.entity.NoticeReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeReplyRepository extends JpaRepository<NoticeReply, Long> {
    Page<NoticeReply> findByNoticeAndDepth(Notice notice, int depth, Pageable pageable);
    List<NoticeReply> findByParent(NoticeReply parent);
    int countByNotice(Notice notice);
}
