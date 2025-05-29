package com.example.gitmanager.util.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.gitmanager.member.service.TokenService;
import com.example.gitmanager.util.exception.ExpiredRefreshTokenException;
import com.example.gitmanager.util.util.JWTUtil;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    private final static int TOKEN_EXPIRED = 498;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null) {
            String refreshTokenHeader = request.getHeader("Refresh-Token");
            if (refreshTokenHeader != null && refreshTokenHeader.startsWith("Bearer ")) {
                String refreshToken = refreshTokenHeader.substring(7);

                String reissueToken = tokenService.reissue(refreshToken);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setHeader("Authorization", "Bearer " + reissueToken);
            }

            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        try {
            if (jwtUtil.validation(token)) {
                String loginID = jwtUtil.getLoginId(token);
                List<String> roles = jwtUtil.getRoles(token);

                List<SimpleGrantedAuthority> auths = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(loginID, null, auths);

                SecurityContextHolder.getContext().setAuthentication(authToken);
                request.setAttribute("loginId", loginID);
            }
        } catch (TokenExpiredException e) {
            response.setStatus(TOKEN_EXPIRED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
        } catch (ExpiredRefreshTokenException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            SecurityContextHolder.clearContext();
        }catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}