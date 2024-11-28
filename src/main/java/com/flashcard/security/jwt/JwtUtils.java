package com.flashcard.security.jwt;

import com.flashcard.constants.Constants;
import com.flashcard.exception.BadRequestException;
import com.flashcard.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${flashSchool.app.jwtSecret}")
    private String jwtSecret;

    @Value("${flashSchool.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getEmail()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) throws Exception {
        String publicKeyString = jwtSecret; // JWT'yi doğrulamak için kullanılan public key
        // Doğrulamak istediğiniz JWT
        String[] parts = authToken.split("\\.");
        String headerPayload = parts[0] + "." + parts[1]; // header + payload kısmı

// Signature kısmını al
        String signature = parts[2];

// Public key'i kullanarak imzayı doğrulamak için RSA doğrulaması yapın
        PublicKey publicKey = getPublicKey(publicKeyString);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(headerPayload.getBytes());

        boolean isValid = sig.verify(Base64.getDecoder().decode(signature));

        if (!isValid) {
            throw new BadRequestException(Constants.TOKEN_NOT_VALID);
        }

        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public PublicKey getPublicKey(String publicKeyString) throws Exception {
        // 1. Public key string'ini Base64 ile decode et
        String publicKeyPEM = publicKeyString.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        // 2. KeyFactory ile PublicKey nesnesi oluştur
        KeyFactory keyFactory = KeyFactory.getInstance("RSA"); // RSA algoritması ile çalışıyoruz
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);

        // 3. PublicKey nesnesini oluştur
        return keyFactory.generatePublic(keySpec);
    }
}
