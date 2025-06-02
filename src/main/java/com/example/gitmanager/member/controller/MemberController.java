package com.example.gitmanager.member.controller;

import com.example.gitmanager.member.dto.member.LoginDTO;
import com.example.gitmanager.member.dto.member.LoginResultDTO;
import com.example.gitmanager.member.dto.member.MemberUpdateDTO;
import com.example.gitmanager.member.dto.member.SignInDTO;
import com.example.gitmanager.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/member")
@RestController
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<LoginResultDTO> login(@RequestBody LoginDTO dto) {
        return ResponseEntity.ok(memberService.login(dto));
    }

    @PostMapping
    public ResponseEntity<Void> signIn(@RequestBody SignInDTO dto) {
        memberService.signIn(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(
            @RequestBody MemberUpdateDTO dto, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        memberService.update(dto, loginId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id, HttpServletRequest request) {
        String loginId = (String) request.getAttribute("loginId");

        memberService.signOut(id, loginId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/check/{type}/{target}")
    public ResponseEntity<Boolean> duplicateCheck(@PathVariable String type, @PathVariable String target) {
        return switch (type) {
            case "name" -> ResponseEntity.ok(memberService.isNameDuplicate(target));
            case "email" -> ResponseEntity.ok(memberService.isEmailDuplicate(target));
            case "loginId" -> ResponseEntity.ok(memberService.isLoginIdDuplicate(target));
            default -> throw new IllegalArgumentException("잘못된 요청입니다.");
        };
    }
}
