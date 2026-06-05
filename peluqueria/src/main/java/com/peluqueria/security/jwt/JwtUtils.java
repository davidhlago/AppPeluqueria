package com.peluqueria.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // TRUCO: Añadimos un valor por defecto tras los dos puntos (:)
    // Esto evita que la app falle si no lee bien el application.properties
    @Value("${peluqueria.jwt.secret:MiClaveSuperSecretaParaJWT1234567890ABCDEF}")
    private String jwtSecret;

    @Value("${peluqueria.jwt.expirationMs:86400000}")
    private int jwtExpirationMs;

    private Key getSigningKey() {
        // Es mejor especificar UTF_8 para evitar problemas de codificación en Windows/Linux
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol) // Guardamos el rol dentro del token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Método extra para sacar el Rol directamente
    public String getRolFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("rol", String.class);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.out.println("Firma JWT no válida: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Token JWT no válido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.out.println("El token JWT ha expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Token JWT no soportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("La cadena claims JWT está vacía: " + e.getMessage());
        }
        return false;
    }
}