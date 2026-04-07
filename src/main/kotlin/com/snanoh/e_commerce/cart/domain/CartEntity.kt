package com.snanoh.e_commerce.cart.domain

import com.snanoh.e_commerce.user.domain.UserEntity
import jakarta.persistence.*

@Entity
@Table(name = "carts")
class CartEntity(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    val user: UserEntity,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cartId: Long = 0L
)
