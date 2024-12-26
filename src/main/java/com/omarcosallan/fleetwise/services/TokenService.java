package com.omarcosallan.fleetwise.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.omarcosallan.fleetwise.domain.user.User;
import com.omarcosallan.fleetwise.exceptions.TokenValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {
    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("fleet-wise-api")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new TokenValidationException("Error while generating token:" + exception.getMessage());
        }
    }

    public UUID validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return UUID.fromString(JWT.require(algorithm).withIssuer("fleet-wise-api").build().verify(token).getSubject());
        } catch (JWTVerificationException exception) {
            throw new TokenValidationException("Invalid token: " + exception.getMessage());
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusDays(7).toInstant(ZoneOffset.of("-03:00"));
    }
}
