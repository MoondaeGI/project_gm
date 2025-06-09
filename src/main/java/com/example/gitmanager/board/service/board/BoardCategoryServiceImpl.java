package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardCategoryDTO;
import com.example.gitmanager.board.dto.board.BoardCategoryInsertDTO;
import com.example.gitmanager.board.dto.board.BoardCategoryUpdateDTO;
import com.example.gitmanager.board.entity.board.BoardCategory;
import com.example.gitmanager.board.repository.board.BoardCategoryRepository;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.enums.Yn;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardCategoryServiceImpl implements BoardCategoryService {
    private final BoardCategoryRepository boardCategoryRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<BoardCategoryDTO> findByProjectId(long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));

        return boardCategoryRepository.findByProject(project).stream()
                .map(BoardCategoryDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public long insert(BoardCategoryInsertDTO dto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", dto.getProjectId())));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);
        if (projectMember == null) {
            throw new UnAuthenticationException();
        }

        BoardCategory parent = (dto.getParentId() != null) ?
                boardCategoryRepository.findById(dto.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException(
                                String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", dto.getParentId()))) : null;

        BoardCategory boardCategory = boardCategoryRepository.save(
                BoardCategory.builder()
                        .name(dto.getName())
                        .project(project)
                        .depth(dto.getDepth())
                        .parent(parent)
                        .build());

        return boardCategory.getId();
    }

    @Transactional
    @Override
    public long update(BoardCategoryUpdateDTO dto, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        BoardCategory boardCategory = boardCategoryRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", dto.getId())));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(boardCategory.getProject(), member);
        if (projectMember.getLeaderYn().equals(Yn.N) || projectMember == null) {
            throw new UnAuthenticationException();
        }

        boardCategory.update(dto.getName());

        return dto.getId();
    }

    @Transactional
    @Override
    public void delete(long id, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        BoardCategory boardCategory = boardCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 게시글 카테고리가 없습니다.", id)));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(boardCategory.getProject(), member);
        if (projectMember.getLeaderYn().equals(Yn.N) || projectMember == null) {
            throw new UnAuthenticationException();
        }

        boardCategoryRepository.delete(boardCategory);
    }
}
