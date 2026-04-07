package com.snanoh.e_commerce.cart.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CartItemRepository : JpaRepository<CartItemEntity, Long> {
    fun findByCartCartIdAndProductProductId(cartId: Long, productId: Long): CartItemEntity?
    fun findByCartCartId(cartId: Long): List<CartItemEntity>
}
