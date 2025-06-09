package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.ReplyDTO;
import com.example.gitmanager.board.dto.board.ReplyInsertDTO;
import com.example.gitmanager.board.dto.board.ReplyUpdateDTO;
import com.example.gitmanager.board.entity.board.Board;
import com.example.gitmanager.board.entity.board.Reply;
import com.example.gitmanager.board.repository.board.BoardRepository;
import com.example.gitmanager.board.repository.board.ReplyRepository;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReplyServiceImp implements ReplyService {
    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public List<ReplyDTO> findByBoardId(long boardId, int page) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글이 없습니다.", boardId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, replyRepository.countByBoard(board));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return replyRepository.findByBoard(board, pageRequest).stream()
                .map(ReplyDTO::of)
                .toList();
    }

    @Override
    public List<ReplyDTO> findByParentId(long parentId) {
        Reply reply = replyRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 댓글이 없습니다.", parentId)));

        return replyRepository.findByParent(reply).stream()
                .map(ReplyDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public long insert(ReplyInsertDTO dto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글이 없습니다.", dto.getBoardId())));

        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(board.getBoardCategory().getProject(), member);
        if (projectMember == null) {
            throw new UnAuthenticationException();
        }

        Reply parent = (dto.getParentId() != null) ?
                replyRepository.findById(dto.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("%d의 번호를 가진 댓글이 없습니다.", dto.getParentId()))) : null;

        Reply reply = replyRepository.save(Reply.builder()
                .board(board)
                .writer(projectMember)
                .content(dto.getContent())
                .parent(parent)
                .depth(dto.getDepth())
                .build());

        return reply.getId();
    }

    @Transactional
    @Override
    public long update(ReplyUpdateDTO dto, String loginId) {
        Reply reply = replyRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 댓글이 없습니다.", dto.getId())));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(reply.getBoard().getBoardCategory().getProject(), member);
        if (projectMember.getId() == reply.getWriter().getId()) {
            reply.update(dto.getContent());
        } else {
            throw new UnAuthenticationException();
        }

        return dto.getId();
    }

    @Override
    public void delete(long id, String loginId) {
        Reply reply = replyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 댓글이 없습니다.", id)));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(reply.getBoard().getBoardCategory().getProject(), member);
        if (projectMember.getId() == reply.getWriter().getId()) {
            replyRepository.delete(reply);
        } else {
            throw new UnAuthenticationException();
        }
    }
}
