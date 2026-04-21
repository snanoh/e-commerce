package com.snanoh.e_commerce.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtProvider {
    // Secret key should be at least 256 bits (32 characters)
    private val secretString = "v9yB&E)H@McQfTjWnZr4u7x!A%C*F-JaNdRgUkXp2s5v8y/B?E(G+KbPeShVmYq"
    private val key: SecretKey = Keys.hmacShaKeyFor(secretString.toByteArray())
    private val expirationTime = 3600000L // 1 hour
    private val refreshExpirationTime = 604800000L // 7 days

    fun createToken(email: String, isAdmin: Boolean): String {
        val now = Date()
        val validity = Date(now.time + expirationTime)

        return Jwts.builder()
            .subject(email)
            .claim("isAdmin", isAdmin)
            .issuedAt(now)
            .expiration(validity)
            .signWith(key)
            .compact()
    }

    fun createRefreshToken(email: String): String {
        val now = Date()
        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(Date(now.time + refreshExpirationTime))
            .signWith(key)
            .compact()
    }

    fun getEmail(token: String): String {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String): Boolean {
        return try {
            val claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
            !claims.payload.expiration.before(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun isAdmin(token: String): Boolean {
        val claims = Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
        return claims.payload["isAdmin"] as Boolean
    }
}
