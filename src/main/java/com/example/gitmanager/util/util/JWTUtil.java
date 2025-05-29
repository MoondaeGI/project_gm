package com.example.gitmanager.util.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
    @Value("${jwt.secret}")
    private String secret;

    public String createToken(String loginID, List<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + 60 * 1000 * 30);

        return JWT.create()
                .withSubject(loginID)
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }

    public boolean validation(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expiresAt = jwt.getExpiresAt();
            if (expiresAt != null && expiresAt.before(new Date())) {
                throw new TokenExpiredException("해당 토큰이 만료되었습니다.", null);
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        }catch(Exception e) {
            return false;
        }
    }

    public String getLoginId(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).build()
                .verify(token);
        return jwt.getSubject();
    }

    public List<String> getRoles(String token) {
        DecodedJWT jwt = JWT.require(Algorithm.HMAC256(secret)).build()
                .verify(token);
        return jwt.getClaim("roles").asList(String.class);
    }

    public String createRefreshToken(String loginId) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + 60 * 1000 * 60 * 24);

        return JWT.create()
                .withSubject(loginId)
                .withIssuedAt(now)
                .withExpiresAt(expiresAt)
                .sign(algorithm);
    }
}