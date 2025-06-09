package com.example.gitmanager.board.service.notice;

import com.example.gitmanager.board.dto.notice.ProjectNoticeDTO;
import com.example.gitmanager.board.dto.notice.ProjectNoticeInsertDTO;
import com.example.gitmanager.board.dto.notice.ProjectNoticeUpdateDTO;
import com.example.gitmanager.board.entity.notice.ProjectNotice;
import com.example.gitmanager.board.entity.notice.ProjectNoticeView;
import com.example.gitmanager.board.repository.notice.ProjectNoticeRepository;
import com.example.gitmanager.board.repository.notice.ProjectNoticeViewRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectNoticeServiceImpl implements ProjectNoticeService {
    private final ProjectNoticeRepository projectNoticeRepository;
    private final ProjectNoticeViewRepository projectNoticeViewRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public List<ProjectNoticeDTO> findByProjectId(long projectId, int page) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", projectId)));

        int start = (page - 1) * 10;
        int end = Math.min(page * 10, projectNoticeRepository.countByProject(project));
        PageRequest pageRequest = PageRequest.of(start, end, Sort.by(Sort.Direction.DESC, "id"));

        return projectNoticeRepository.findByProject(project, pageRequest).stream()
                .map(ProjectNoticeDTO::of)
                .toList();
    }

    @Transactional
    @Override
    public ProjectNoticeDTO findById(long id, String loginId) {
        ProjectNotice projectNotice = projectNoticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트 공지사항이 없습니다.", id)));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(projectNotice.getProject(), member);
        if (projectMember == null) {
            throw new UnAuthenticationException();
        }

        if (!projectNoticeViewRepository
                .existsByProjectNoticeAndProjectMember(projectNotice, projectMember)) {
            projectNoticeViewRepository.save(ProjectNoticeView.builder()
                    .projectMember(projectMember)
                    .projectNotice(projectNotice)
                    .build());
        }

        return ProjectNoticeDTO.of(projectNotice);
    }

    @Override
    public long insert(ProjectNoticeInsertDTO dto, String loginId) {
        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트가 없습니다.", dto.getProjectId())));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository.findByProjectAndMember(project, member);
        if (projectMember == null || projectMember.getLeaderYn().equals(Yn.N)) {
            throw new UnAuthenticationException();
        }

        ProjectNotice projectNotice = projectNoticeRepository.save(ProjectNotice.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .project(project)
                .build());

        return projectNotice.getId();
    }

    @Transactional
    @Override
    public long update(ProjectNoticeUpdateDTO dto, String loginId) {
        ProjectNotice projectNotice = projectNoticeRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트 공지사항이 없습니다.", dto.getId())));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(projectNotice.getProject(), member);
        if (projectMember == null || projectMember.getLeaderYn().equals(Yn.N)) {
            throw new UnAuthenticationException();
        }

        projectNotice.update(dto.getTitle(), dto.getContent());

        return dto.getId();
    }

    @Override
    public void delete(long id, String loginId) {

    }

    @Transactional
    @Override
    public void toggleYn(long id, String loginId) {
        ProjectNotice projectNotice = projectNoticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%d의 번호를 가진 프로젝트 공지사항이 없습니다.", id)));

        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException(
                        String.format("%s의 아이디를 가진 회원이 없습니다.", loginId)));
        ProjectMember projectMember = projectMemberRepository
                .findByProjectAndMember(projectNotice.getProject(), member);
        if (projectMember.getLeaderYn().equals(Yn.Y)) {
            projectNotice.toggleOpenYn();
        } else {
            throw new UnAuthenticationException();
        }
    }
}
