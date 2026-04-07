package com.snanoh.e_commerce.user.service

import com.snanoh.e_commerce.user.domain.UserEntity
import com.snanoh.e_commerce.user.domain.UserRepository
import com.snanoh.e_commerce.user.dto.LoginRequest
import com.snanoh.e_commerce.user.dto.LoginResponse
import com.snanoh.e_commerce.user.dto.UserCreateRequest
import com.snanoh.e_commerce.user.dto.UserResponse
import com.snanoh.e_commerce.user.dto.UserUpdateRequest
import com.snanoh.e_commerce.security.JwtProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtProvider: JwtProvider,
    @Lazy private val authenticationManager: AuthenticationManager
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username)
            ?: throw UsernameNotFoundException("User not found: $username")
    }

    @Transactional(readOnly = true)
    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { UserResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }
        return UserResponse.from(user)
    }

    @Transactional
    fun createUser(request: UserCreateRequest): UserResponse {
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email already exists")
        }
        val user = UserEntity(
            email = request.email,
            name = request.name,
            address = request.address,
            password = passwordEncoder.encode(request.password)
        )
        val saved = userRepository.save(user)
        return UserResponse.from(saved)
    }

    @Transactional
    fun login(request: LoginRequest): LoginResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )

        val user = authentication.principal as UserEntity
        user.lastLoginTime = Instant.now()

        val token = jwtProvider.createToken(user.email, user.isAdmin)
        return LoginResponse(token = token, email = user.email, name = user.name, isAdmin = user.isAdmin)
    }

    @Transactional
    fun updateUser(id: Long, request: UserUpdateRequest): UserResponse {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }

        request.name?.let { user.name = it }
        request.address?.let { user.address = it }
        request.password?.let { user.updatePassword(passwordEncoder.encode(it)) }

        return UserResponse.from(user)
    }

    @Transactional
    fun deleteUser(id: Long) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("User not found")
        }
    }

    @Transactional
    fun grantAdminRole(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow { IllegalArgumentException("User not found") }
        user.isAdmin = true
        return UserResponse.from(user)
    }
}
