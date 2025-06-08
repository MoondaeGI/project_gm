package com.example.gitmanager.board.service.board;

import com.example.gitmanager.board.dto.board.BoardCategoryDTO;
import com.example.gitmanager.board.entity.board.BoardCategory;
import com.example.gitmanager.board.repository.board.BoardCategoryRepository;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.project.repository.ProjectMemberRepository;
import com.example.gitmanager.project.repository.ProjectRepository;
import com.example.gitmanager.util.exception.UnAuthenticationException;
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

    @Override
    public void insert(String name, long projectId, String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));

        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);
        if (projectMember == null) {
            throw new UnAuthenticationException();
        }

        BoardCategory boardCategory = boardCategoryRepository.save(
                BoardCategory.builder()
                        .name(name)
                        .project(project)
                        .build());
    }

    @Override
    public void update(String name, String loginId) {

    }

    @Override
    public void delete(long id, String loginId) {

    }
}
