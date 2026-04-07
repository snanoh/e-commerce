package com.snanoh.e_commerce.order.service

import com.snanoh.e_commerce.delivery.domain.DeliveryEntity
import com.snanoh.e_commerce.order.domain.*
import com.snanoh.e_commerce.order.dto.OrderCreateRequest
import com.snanoh.e_commerce.order.dto.OrderResponse
import com.snanoh.e_commerce.product.ProductRepository
import com.snanoh.e_commerce.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    @Transactional(readOnly = true)
    fun getAllOrders(): List<OrderResponse> {
        return orderRepository.findAll().map { OrderResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getOrderById(id: Long): OrderResponse {
        val order = orderRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Order not found") }
        return OrderResponse.from(order)
    }

    @Transactional
    fun createOrder(userEmail: String, request: OrderCreateRequest): OrderResponse {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found")

        // 1. Create Delivery
        val delivery = DeliveryEntity(
            recipientName = request.recipientName,
            recipientPhone = request.recipientPhone,
            address = request.address
        )

        // 2. Create Order
        val order = OrderEntity(
            user = user,
            delivery = delivery
        )

        // 3. Create OrderItems and Deduct Stock
        request.items.forEach { itemRequest ->
            val product = productRepository.findById(itemRequest.productId)
                .orElseThrow { IllegalArgumentException("Product not found: ${itemRequest.productId}") }
            
            // Deduct stock
            product.removeStock(itemRequest.count)

            val orderItem = OrderItemEntity(
                product = product,
                orderPrice = product.price,
                count = itemRequest.count
            )
            order.addOrderItem(orderItem)
        }

        val savedOrder = orderRepository.save(order)
        return OrderResponse.from(savedOrder)
    }

    @Transactional
    fun cancelOrder(id: Long) {
        val order = orderRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Order not found") }
        
        if (order.status != OrderStatus.ORDERED) {
            throw IllegalStateException("Cannot cancel order in status: ${order.status}")
        }

        order.status = OrderStatus.CANCELLED
        // Optional: Restore stock logic could be added here
    }
}
