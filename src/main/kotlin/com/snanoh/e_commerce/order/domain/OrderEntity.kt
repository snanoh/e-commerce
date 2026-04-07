package com.snanoh.e_commerce.order.domain

import com.snanoh.e_commerce.delivery.domain.DeliveryEntity
import com.snanoh.e_commerce.user.domain.UserEntity
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "orders")
class OrderEntity(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "delivery_id", nullable = false)
    val delivery: DeliveryEntity,

    @Column(nullable = false)
    var orderDate: Instant = Instant.now(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.ORDERED,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    val orderItems: MutableList<OrderItemEntity> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val orderId: Long = 0L
) {
    fun addOrderItem(orderItem: OrderItemEntity) {
        orderItems.add(orderItem)
        orderItem.order = this
    }
}
