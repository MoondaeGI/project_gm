package com.example.gitmanager.notice.service;

import com.example.gitmanager.notice.dto.reply.NoticeReplyDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyInsertDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyUpdateDTO;

import java.util.List;

public interface NoticeReplyService {
    List<NoticeReplyDTO> findByNoticeId(long noticeId, int page);
    void insert(NoticeReplyInsertDTO dto);
    void update(NoticeReplyUpdateDTO dto, String loginId);
    void delete(long dto, String loginId);
}
