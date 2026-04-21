package com.snanoh.e_commerce.user.dto

import com.snanoh.e_commerce.user.domain.UserEntity
import java.time.Instant

data class UserCreateRequest(
    val email: String,
    val name: String,
    val address: String,
    val password: String
)

data class UserUpdateRequest(
    val name: String?,
    val address: String?,
    val password: String?
)

data class UserResponse(
    val userId: Long,
    val email: String,
    val name: String,
    val address: String,
    val lastLoginTime: Instant?,
    val isAdmin: Boolean
) {
    companion object {
        fun from(user: UserEntity): UserResponse {
            return UserResponse(
                userId = user.userId,
                email = user.email,
                name = user.name,
                address = user.address,
                lastLoginTime = user.lastLoginTime,
                isAdmin = user.isAdmin
            )
        }
    }
}
data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val refreshToken: String,
    val email: String,
    val name: String,
    val isAdmin: Boolean
)

data class RefreshRequest(
    val refreshToken: String
)

data class RefreshResponse(
    val token: String
)
