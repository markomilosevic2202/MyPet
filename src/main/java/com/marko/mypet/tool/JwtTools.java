package com.marko.mypet.tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTools {
    public static String getAccessToken(String subject, Collection<SimpleGrantedAuthority> authorities, String issuer) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) //One Day
                .withIssuer(issuer)
                .withClaim("roles", authorities
                        .stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(Algorithm.HMAC512("SUPER_SECRET".getBytes()));
    }

    public static String getRefreshToken(String subject, String issuer) {
        return JWT.create()
                .withSubject(subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + 31556952000L)) //One Year
                .withIssuer(issuer)
                .sign(Algorithm.HMAC512("SUPER_SECRET".getBytes()));
    }

    public static DecodedJWT getDecodedToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512("SUPER_SECRET".getBytes())).build();
        return verifier.verify(token);
    }

    public static Boolean verifyWithEmail(String authorizationHeader, String email) {
        DecodedJWT decodedJWT = JwtTools.getDecodedToken(authorizationHeader);
        return decodedJWT.getSubject().equals(email);
    }

    public static String getEmailFromOAuthToken(Jwt jwt) throws Exception {
        String email = jwt.getClaim("email").toString();
        if (email == null || email.isEmpty()) {
            throw new Exception("Email not present in claims");
        }
        return email;
    }

    public static String getClaimFromOAuthToken(Jwt jwt, String claim) throws Exception {
        String email = jwt.getClaim(claim).toString();
        if (email == null || email.isEmpty()) {
            throw new Exception(claim + " not present in claims...");
        }
        return email;
    }
}

