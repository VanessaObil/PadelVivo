package com.padel.api_padel.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PublicKey;

@Service
public class JwtService {

    @Autowired
    private KeyPair jwtKeyPair;

    public Long getUserIdFromToken(String token) {
        PublicKey publicKey = jwtKeyPair.getPublic();

        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Aquí asumo que el userId está guardado en el claim "id"
        Object idClaim = claims.get("id");

        if (idClaim instanceof Integer) {
            return ((Integer) idClaim).longValue();
        } else if (idClaim instanceof Long) {
            return (Long) idClaim;
        } else if (idClaim instanceof String) {
            return Long.parseLong((String) idClaim);
        } else {
            throw new RuntimeException("No se pudo extraer el userId del token");
        }
    }
}
