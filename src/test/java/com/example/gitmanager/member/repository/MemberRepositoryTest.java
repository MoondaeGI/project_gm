package com.example.gitmanager.member.repository;

import com.example.gitmanager.member.entity.Member;
import com.example.gitmanager.util.util.PasswordUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class MemberRepositoryTest {
    @Autowired private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    public void setup() {
        member = Member.builder()
                .loginId("test")
                .name("test")
                .email("<EMAIL>")
                .profileImg("test")
                .password("test")
                .build();
        memberRepository.save(member);
    }

    @AfterEach
    public void teardown() {
        memberRepository.delete(member);
    }

    @DisplayName("로그인 아이디 중복 쿼리 테스트")
    @Test
    public void existsByLoginIdTest() {
        assert memberRepository.existsByLoginId("test");
    }
}
