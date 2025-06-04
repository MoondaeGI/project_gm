package com.example.gitmanager.member.service;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.entity.Token;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.exception.ExpiredRefreshTokenException;
import com.example.gitmanager.util.util.JWTUtil;
import com.example.gitmanager.util.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TokenServiceTest {
    @Autowired private TokenService tokenService;
    @Autowired private TokenRepository tokenRepository;
    @Autowired private MemberRepository memberRepository;

    @Autowired private PasswordUtil passwordUtil;
    @Autowired private JWTUtil jwtUtil;

    private Member member;
    private Token token;
    private String refreshToken;

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

        refreshToken = jwtUtil.createRefreshToken(member.getLoginId());
        token = Token.builder()
                .member(member)
                .token(refreshToken)
                .build();
        tokenRepository.save(token);
    }

    @AfterEach
    public void teardown() {
        memberRepository.delete(member);
    }

    @DisplayName("토큰 재발급 테스트")
    @Test
    public void reissueTest1() {
        String reissueToken = tokenService.reissue(refreshToken);

        assertThat(jwtUtil.validation(reissueToken)).isTrue();
        assertThat(jwtUtil.getLoginId(reissueToken)).isEqualTo(member.getLoginId());
        assertThat(tokenRepository.findByToken(refreshToken).get().getReissueCount())
                .isEqualTo(1);
    }

    @DisplayName("유효 토큰이 아닌 토큰을 가져온 경우")
    @Test
    public void reissueTest2() {
        String unExpectedToken = jwtUtil.createRefreshToken("unExpected");

        assertThrows(IllegalArgumentException.class, () ->
                tokenService.reissue(unExpectedToken));
    }

    @DisplayName("재발급 횟수 테스트")
    @Test
    public void reissueTest3() {
        while(true) {
            try {
                tokenService.reissue(refreshToken);
            } catch (ExpiredRefreshTokenException e) {
                break;
            }
        }

        int reissueCount = tokenRepository.findByToken(refreshToken).get().getReissueCount();

        assertThat(reissueCount).isGreaterThan(9);
    }

    @DisplayName("ExpiredRefreshTokenException의 예외 처리 확인")
    @Test
    public void reissueTest4() {
        while(true) {
            try {
                tokenService.reissue(refreshToken);
            } catch (ExpiredRefreshTokenException e) {
                assertThat(e.getRefreshToken()).isEqualTo(refreshToken);
                assertThat(e.getMessage()).isEqualTo("refresh token이 만료되었습니다.");
                break;
            }
        }
    }
}
