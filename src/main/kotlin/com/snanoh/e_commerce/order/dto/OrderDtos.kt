package com.snanoh.e_commerce.order.dto

import com.snanoh.e_commerce.order.domain.OrderEntity
import com.snanoh.e_commerce.order.domain.OrderStatus
import java.time.Instant

data class OrderItemRequest(
    val productId: Long,
    val count: Int
)

data class OrderCreateRequest(
    val recipientName: String,
    val recipientPhone: String,
    val address: String,
    val items: List<OrderItemRequest>
)

data class OrderItemResponse(
    val productId: Long,
    val productName: String,
    val orderPrice: Long,
    val count: Int
)

data class OrderResponse(
    val orderId: Long,
    val orderDate: Instant,
    val status: OrderStatus,
    val recipientName: String,
    val recipientPhone: String,
    val address: String,
    val items: List<OrderItemResponse>,
    val totalAmount: Long
) {
    companion object {
        fun from(entity: OrderEntity): OrderResponse {
            val items = entity.orderItems.map { item ->
                OrderItemResponse(
                    productId = item.product.productId,
                    productName = item.product.name,
                    orderPrice = item.orderPrice,
                    count = item.count
                )
            }
            return OrderResponse(
                orderId = entity.orderId,
                orderDate = entity.orderDate,
                status = entity.status,
                recipientName = entity.delivery.recipientName,
                recipientPhone = entity.delivery.recipientPhone,
                address = entity.delivery.address,
                items = items,
                totalAmount = items.sumOf { it.orderPrice * it.count }
            )
        }
    }
}
