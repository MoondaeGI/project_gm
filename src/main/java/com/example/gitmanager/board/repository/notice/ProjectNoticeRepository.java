package com.example.gitmanager.board.repository.notice;

import com.example.gitmanager.board.entity.notice.ProjectNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectNoticeRepository extends JpaRepository<ProjectNotice, Long> {
}
