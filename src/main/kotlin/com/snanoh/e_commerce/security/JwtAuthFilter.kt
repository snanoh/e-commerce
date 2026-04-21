package com.snanoh.e_commerce.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtProvider: JwtProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(JwtAuthFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val method = request.method
        val uri = request.requestURI
        val authHeader = request.getHeader("Authorization")

        val token = authHeader?.let {
            if (it.startsWith("Bearer ")) it.substring(7) else null
        }

        when {
            authHeader == null -> log.debug("[JWT] $method $uri — Authorization 헤더 없음, 익명 요청")
            token == null -> log.warn("[JWT] $method $uri — 잘못된 Authorization 형식: '$authHeader'")
            !jwtProvider.validateToken(token) -> log.warn("[JWT] $method $uri — 토큰 유효성 검사 실패 (만료 또는 변조)")
            else -> {
                val email = jwtProvider.getEmail(token)
                val userDetails = userDetailsService.loadUserByUsername(email)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )
                SecurityContextHolder.getContext().authentication = authentication
                log.debug("[JWT] $method $uri — 인증 성공: email=$email, roles=${userDetails.authorities}")
            }
        }

        filterChain.doFilter(request, response)
    }
}
