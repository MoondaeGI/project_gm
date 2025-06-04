package com.example.gitmanager.project.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.project.entity.Project;
import com.example.gitmanager.project.entity.ProjectMember;
import com.example.gitmanager.util.enums.ProjectType;
import com.example.gitmanager.util.enums.Yn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class ProjectRepositoryTest {
    @Autowired private MemberRepository memberRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ProjectMemberRepository projectMemberRepository;

    private Project project;
    private Member member;
    private ProjectMember projectMember;

    @BeforeEach
    public void setup() {
        member = Member.builder()
                .loginId("test")
                .name("test")
                .email("<EMAIL>")
                .profileImg("test")
                .build();
        memberRepository.save(member);

        project = Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build();
        projectRepository.save(project);

        projectMember = ProjectMember.builder()
                .project(project)
                .member(member)
                .leaderYn(Yn.Y)
                .build();
        projectMemberRepository.save(projectMember);
    }

    @AfterEach
    public void teardown() {
        projectRepository.delete(project);
        memberRepository.delete(member);
    }

    @DisplayName("findByMember 쿼리 테스트")
    @Test
    public void findByMemberTest() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Project> projectPage = projectRepository.findByMember(member, pageRequest);

        assertThat(projectPage.getContent().size())
                .isEqualTo(1);
    }

    @DisplayName("countByMember 쿼리 테스트")
    @Test
    public void countByMemberTest1() {
        long count = projectRepository.countByMember(member);

        assertThat(count).isEqualTo(1);
    }

    @DisplayName("countByMember 쿼리 테스트")
    @Test
    public void countByMemberTest2() {
        Project addedProject = Project.builder()
                .name("test")
                .description("test")
                .url("test")
                .type(ProjectType.PUBLIC)
                .build();
        projectRepository.save(addedProject);

        ProjectMember addedProjectMember = ProjectMember.builder()
                .project(addedProject)
                .member(member)
                .leaderYn(Yn.Y)
                .build();
        projectMemberRepository.save(addedProjectMember);

        assertThat(projectRepository.countByMember(member)).isEqualTo(2);

        // teardown
        projectRepository.delete(addedProject);
    }
}
