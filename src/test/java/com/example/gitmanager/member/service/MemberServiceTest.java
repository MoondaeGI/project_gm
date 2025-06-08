package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import com.example.gitmanager.util.util.JWTUtil;
import com.example.gitmanager.util.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberServiceTest {
    @Autowired private MemberService memberService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TokenRepository tokenRepository;

    @Autowired private PasswordUtil passwordUtil;
    @Autowired private JWTUtil jwtUtil;

    private Member member;

    @BeforeEach
    public void setup() {
        member = Member.builder()
                .loginId("test")
                .name("test")
                .email("<EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    public void teardown() {
       memberRepository.delete(member);
    }

    @DisplayName("회원가입 테스트")
    @Transactional
    @Test
    public void signInTest() throws IOException {
        SignInDTO dto = SignInDTO.builder()
                .loginId("signupTest")
                .name("test")
                .email("<EMAIL>")
                .profileImg("test")
                .password("test")
                .build();
        memberService.signIn(dto);

        Member signupTestMember = memberRepository.findByLoginId("signupTest").get();

        assertThat(signupTestMember).isNotNull();
        assertThat(signupTestMember).isEqualTo("test");

        //teardown
        memberRepository.delete(member);
    }

    @DisplayName("로그인 유저 일치 확인")
    @Test
    public void loginTest1() {
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();

        assertThat(memberService.login(dto).getId()).isEqualTo(member.getId());
    }

    @DisplayName("로그인 토큰 확인")
    @Test
    public void loginTest2() {
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String token = memberService.login(dto).getToken();

        assertThat(jwtUtil.getLoginId(token)).isEqualTo("test");
    }

    @DisplayName("로그인 후 리프레시 토큰 확인")
    @Test
    public void loginTest3() {
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String refreshToken = memberService.login(dto).getRefreshToken();

        assertThat(jwtUtil.getLoginId(refreshToken)).isEqualTo("test");
    }

    @DisplayName("리프레시 토큰 db 저장 확인")
    @Test
    public void loginTest4() {
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String refreshToken = memberService.login(dto).getRefreshToken();

        assertThat(tokenRepository.findByToken(refreshToken).get().getMember().getId())
                .isEqualTo(member.getId());
    }

    @DisplayName("로그인 아이디 중복 체크 테스트")
    @Test
    public void isLoginIdDuplicateTest() {
        assertThat(memberService.isLoginIdDuplicate("test")).isTrue();
        assertThat(memberService.isLoginIdDuplicate("test2")).isFalse();
    }

    @DisplayName("업데이트 테스트")
    @Test
    public void updateTest() throws IOException {
        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();
        memberService.update(dto, member.getLoginId());

        assertThat(memberRepository.findById(member.getId()).get().getName())
                .isEqualTo("update test");
    }

    @DisplayName("본인 외 업데이트 테스트")
    @Test
    public void updateTest2() {
        // 비정상 접근 유저 생성
        Member testMember = Member.builder()
                .loginId("not allowed")
                .name("not allowed test")
                .email("<test EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .build();
        memberRepository.save(testMember);

        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();

        assertThrows(UnAuthenticationException.class, () ->
                memberService.update(dto, "not allowed"));
        assertThat(memberRepository.findById(member.getId()).get().getName())
                .isNotEqualTo("update test");

        // teardown
        memberRepository.delete(testMember);
    }

    @DisplayName("운영자 접근 통과 확인")
    @Test
    public void updateTest3() throws IOException {
        Member adminMember = Member.builder()
                .loginId("admin")
                .name("admin")
                .email("<admin EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .role(ROLE.ADMIN)
                .build();
        memberRepository.save(adminMember);

        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();
        memberService.update(dto, adminMember.getLoginId());

        assertThat(memberRepository.findById(member.getId()).get().getName())
                .isEqualTo("update test");

        memberRepository.delete(adminMember);
    }

    @DisplayName("회원 탈퇴 확인")
    @Test
    public void signOutTest() {
        memberService.signOut(member.getId(), member.getLoginId());

        assertThat(memberRepository.findById(member.getId()).isEmpty()).isTrue();
    }

    @DisplayName("본인 외 회원탈퇴 테스트")
    @Test
    public void signOutTest2() {
        // 비정상 접근 유저 생성
        Member testMember = Member.builder()
                .loginId("not allowed")
                .name("not allowed test")
                .email("<test EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .build();
        memberRepository.save(testMember);

        assertThrows(UnAuthenticationException.class, () ->
                memberService.signOut(member.getId(), "not allowed"));

        // teardown
        memberRepository.delete(testMember);
    }

    @DisplayName("운영자 회원 탈퇴 접근 가능 확인")
    @Test
    public void sigOutTest3() {
        Member adminMember = Member.builder()
                .loginId("admin")
                .name("admin")
                .email("<admin EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .role(ROLE.ADMIN)
                .build();
        memberRepository.save(adminMember);

        memberService.signOut(member.getId(), adminMember.getLoginId());

        assertThat(memberRepository.findById(member.getId()).isEmpty()).isTrue();

        // teardown
        memberRepository.delete(adminMember);
    }
}
