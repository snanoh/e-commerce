package com.snanoh.e_commerce.cart.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartRepository : JpaRepository<CartEntity, Long> {
    fun findByUserUserId(userId: Long): CartEntity?
}
