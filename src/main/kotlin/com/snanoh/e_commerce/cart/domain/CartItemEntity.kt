package com.snanoh.e_commerce.cart.domain

import com.snanoh.e_commerce.product.ProductEntity
import jakarta.persistence.*

@Entity
@Table(name = "cart_items")
class CartItemEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    val cart: CartEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: ProductEntity,

    @Column(nullable = false)
    var count: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val cartItemId: Long = 0L
)
