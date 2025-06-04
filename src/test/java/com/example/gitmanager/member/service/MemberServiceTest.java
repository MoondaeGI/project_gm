package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.util.JWTUtil;
import com.example.gitmanager.util.util.PasswordUtil;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    public void signInTest() {
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
}
