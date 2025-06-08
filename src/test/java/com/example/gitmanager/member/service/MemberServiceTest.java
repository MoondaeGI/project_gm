package com.example.gitmanager.member.service;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.LoginResultDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;
import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.member.repository.MemberRepository;
import com.example.gitmanager.member.repository.TokenRepository;
import com.example.gitmanager.util.enums.ROLE;
import com.example.gitmanager.util.exception.UnAuthenticationException;
import com.example.gitmanager.util.util.JWTUtil;
import com.example.gitmanager.util.util.PasswordUtil;
import com.google.cloud.storage.Storage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

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
    @Autowired private Storage storage;

    @Value("${gcp.bucket.name}") private String bucketName;

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
        // when
        SignInDTO dto = SignInDTO.builder()
                .loginId("signupTest")
                .name("test")
                .email("<EMAIL>")
                .profileImg("test")
                .password("test")
                .build();
        memberService.signIn(dto);

        Member signupTestMember = memberRepository.findByLoginId("signupTest").get();

        // then
        assertThat(signupTestMember).isNotNull();
        assertThat(signupTestMember).isEqualTo("test");

        //teardown
        memberRepository.delete(member);
    }

    @DisplayName("로그인 유저 일치 확인")
    @Test
    public void loginTest1() {
        // when
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        LoginResultDTO result = memberService.login(dto);

        // then
        assertThat(result.getId()).isEqualTo(member.getId());
    }

    @DisplayName("로그인 토큰 확인")
    @Test
    public void loginTest2() {
        // when
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String token = memberService.login(dto).getToken();

        // then
        assertThat(jwtUtil.getLoginId(token)).isEqualTo("test");
    }

    @DisplayName("로그인 후 리프레시 토큰 확인")
    @Test
    public void loginTest3() {
        // when
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String refreshToken = memberService.login(dto).getRefreshToken();

        // then
        assertThat(jwtUtil.getLoginId(refreshToken)).isEqualTo("test");
    }

    @DisplayName("리프레시 토큰 db 저장 확인")
    @Test
    public void loginTest4() {
        // when
        LoginDTO dto = LoginDTO.builder()
                .loginId("test")
                .password("test")
                .build();
        String refreshToken = memberService.login(dto).getRefreshToken();

        // then
        assertThat(tokenRepository.findByToken(refreshToken).get().getMember().getId())
                .isEqualTo(member.getId());
    }

    @DisplayName("로그인 아이디 중복 체크 중복일 경우 테스트")
    @Test
    public void isLoginIdDuplicateTest() {
        //given
        String loginId = "test";

        // when
        boolean isDuplicate = memberService.isLoginIdDuplicate(loginId);

        // then
        assertThat(isDuplicate).isTrue();
    }

    @DisplayName("로그인 아이디 중복 체크 중복이 아닐 경우 테스트")
    @Test
    public void isLoginIdDuplicateTest2() {
        //given
        String loginId = "test2";

        // when
        boolean isDuplicate = memberService.isLoginIdDuplicate(loginId);

        // then
        assertThat(isDuplicate).isTrue();
    }

    @DisplayName("업데이트 테스트")
    @Test
    public void updateTest() throws IOException {
        //when
        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();
        memberService.update(dto, member.getLoginId());

        // then
        assertThat(memberRepository.findById(member.getId()).get().getName())
                .isEqualTo("update test");
    }

    @DisplayName("본인 외 업데이트 테스트")
    @Test
    public void updateTest2() {
        // given
        Member testMember = Member.builder()
                .loginId("not allowed")
                .name("not allowed test")
                .email("<test EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .build();
        memberRepository.save(testMember);

        // when
        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();

        // then
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
        // given
        Member adminMember = Member.builder()
                .loginId("admin")
                .name("admin")
                .email("<admin EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .role(ROLE.ADMIN)
                .build();
        memberRepository.save(adminMember);

        // when
        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .profileImg("update test")
                .build();
        memberService.update(dto, adminMember.getLoginId());

        Member updatedMember = memberRepository.findById(adminMember.getId()).get();

        // then
        assertThat(updatedMember.getName()).isEqualTo("update test");

        // teardown
        memberRepository.delete(adminMember);
    }

    @DisplayName("이미지 파일 변경 테스트")
    @Test
    public void updateTest4() throws IOException {
        // given
        MockMultipartFile testFile =
                new MockMultipartFile("file", "test.txt", "text/plain", "test".getBytes());

        // when
        MemberUpdateDTO dto = MemberUpdateDTO.builder()
                .id(member.getId())
                .name("update test")
                .multipartFile(testFile)
                .build();
        memberService.update(dto, member.getLoginId());
        String filePath = memberRepository.findById(member.getId()).get().getProfileImg()
                .replace("https://storage.googleapis.com/" + bucketName + "/", "");

        // then
        assertThat(storage.get(bucketName, filePath).exists()).isTrue();
        assertThat(member.getProfileImg())
                .isNotEqualTo(memberRepository.findById(member.getId()).get().getProfileImg());

        // teardown
        storage.delete(bucketName, filePath);
    }

    @DisplayName("회원 탈퇴 확인")
    @Test
    public void signOutTest() {
        // when
        memberService.signOut(member.getId(), member.getLoginId());

        // then
        assertThrows(IllegalArgumentException.class, () ->
                memberRepository.findById(member.getId()));
    }

    @DisplayName("본인 외 회원탈퇴 테스트")
    @Test
    public void signOutTest2() {
        // given
        Member testMember = Member.builder()
                .loginId("not allowed")
                .name("not allowed test")
                .email("<test EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .build();
        memberRepository.save(testMember);

        // then
        assertThrows(UnAuthenticationException.class, () ->
                memberService.signOut(member.getId(), "not allowed"));

        // teardown
        memberRepository.delete(testMember);
    }

    @DisplayName("운영자 회원 탈퇴 접근 가능 확인")
    @Test
    public void sigOutTest3() {
        // given
        Member adminMember = Member.builder()
                .loginId("admin")
                .name("admin")
                .email("<admin EMAIL>")
                .profileImg("test")
                .password(passwordUtil.encodePassword("test"))
                .role(ROLE.ADMIN)
                .build();
        memberRepository.save(adminMember);

        // when
        memberService.signOut(member.getId(), adminMember.getLoginId());

        // then
        assertThat(memberRepository.findById(member.getId()).isEmpty()).isTrue();

        // teardown
        memberRepository.delete(adminMember);
    }
}
