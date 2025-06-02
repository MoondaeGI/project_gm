package com.example.gitmanager.notice.service;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.notice.dto.reply.NoticeReplyDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyInsertDTO;
import com.example.gitmanager.notice.dto.reply.NoticeReplyUpdateDTO;
import com.example.gitmanager.notice.entity.Notice;
import com.example.gitmanager.notice.entity.NoticeReply;
import com.example.gitmanager.notice.repository.NoticeReplyRepository;
import com.example.gitmanager.notice.repository.NoticeRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticeReplyServiceImpl implements NoticeReplyService {
    private final NoticeReplyRepository noticeReplyRepository;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<NoticeReplyDTO> findByNoticeId(long noticeId, int page) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 번호를 가진 공지사항이 없습니다.")));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, noticeReplyRepository.countByNotice(notice));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        List<NoticeReply> noticeReplyList = noticeReplyRepository.findByNoticeAndDepth(notice, 0, pageRequest)
                .getContent();
        noticeReplyList.forEach(noticeReply -> {
            noticeReplyList.addAll(noticeReplyRepository.findByParent(noticeReply));
        });

        return noticeReplyList.stream()
                .map(NoticeReplyDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public void insert(NoticeReplyInsertDTO dto) {
        Notice notice = noticeRepository.findById(dto.getNoticeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 공지사항이 없습니다.")));
        Member member = memberRepository.findById(dto.getWriterId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 유저가 없습니다.", dto.getWriterId())));

        NoticeReply noticeReply = NoticeReply.builder()
                .notice(notice)
                .writer(member)
                .content(dto.getContent())
                .build();
        noticeReplyRepository.save(noticeReply);
    }

    @Transactional
    @Override
    public void update(NoticeReplyUpdateDTO dto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s를 가진 회원이 없습니다.", loginId)));

        if (member.getId() != dto.getWriterId() && !member.getRole().toString().equals("ADMIN")) {
            throw new UnAuthenticationException();
        }

        NoticeReply noticeReply = noticeReplyRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d를 가진 댓글이 없습니다.")));
        noticeReply.update(dto.getContent());
    }

    @Override
    public void delete(long id, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s를 가진 회원이 없습니다.", loginId)));

        NoticeReply noticeReply = noticeReplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d를 가진 댓글이 없습니다.", id)));

        if (member.getId() != noticeReply.getWriter().getId() && !member.getRole().toString().equals("ADMIN")) {
            throw new UnAuthenticationException();
        }

        noticeReplyRepository.delete(noticeReply);
    }
}
