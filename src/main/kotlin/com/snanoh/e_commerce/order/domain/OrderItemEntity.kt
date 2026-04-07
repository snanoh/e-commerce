package com.snanoh.e_commerce.order.domain

import com.snanoh.e_commerce.product.ProductEntity
import jakarta.persistence.*

@Entity
@Table(name = "order_items")
class OrderItemEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    val product: ProductEntity,

    @Column(nullable = false)
    val orderPrice: Long,

    @Column(nullable = false)
    val count: Int,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderItemId: Long = 0L
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: OrderEntity? = null
}
