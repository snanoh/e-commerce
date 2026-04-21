package com.snanoh.e_commerce.user.domain

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.Instant

@Entity
@Table(name = "users")
class UserEntity(
    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var address: String,

    @Column(nullable = false)
    private var password: String,

    @Column(nullable = false)
    var isAdmin: Boolean = false,

    var lastLoginTime: Instant? = null

    var refreshToken: String? = null,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val userId: Long = 0L
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
        if (isAdmin) authorities.add(SimpleGrantedAuthority("ROLE_ADMIN"))
        return authorities
    }

    override fun getPassword(): String = password

    fun updatePassword(encoded: String) { password = encoded }

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
}
