package com.snanoh.e_commerce.delivery.service

import com.snanoh.e_commerce.delivery.domain.DeliveryEntity
import com.snanoh.e_commerce.delivery.domain.DeliveryRepository
import com.snanoh.e_commerce.delivery.dto.DeliveryCreateRequest
import com.snanoh.e_commerce.delivery.dto.DeliveryResponse
import com.snanoh.e_commerce.delivery.dto.DeliveryUpdateRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DeliveryService(
    private val deliveryRepository: DeliveryRepository
) {
    @Transactional(readOnly = true)
    fun getAllDeliveries(): List<DeliveryResponse> {
        return deliveryRepository.findAll().map { DeliveryResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getDeliveryById(id: Long): DeliveryResponse {
        val delivery = deliveryRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Delivery not found") }
        return DeliveryResponse.from(delivery)
    }

    @Transactional
    fun createDelivery(request: DeliveryCreateRequest): DeliveryResponse {
        val entity = DeliveryEntity(
            recipientName = request.recipientName,
            recipientPhone = request.recipientPhone,
            address = request.address
        )
        val saved = deliveryRepository.save(entity)
        return DeliveryResponse.from(saved)
    }

    @Transactional
    fun updateDelivery(id: Long, request: DeliveryUpdateRequest): DeliveryResponse {
        val delivery = deliveryRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Delivery not found") }

        request.recipientName?.let { delivery.recipientName = it }
        request.recipientPhone?.let { delivery.recipientPhone = it }
        request.address?.let { delivery.address = it }
        request.status?.let { delivery.status = it }
        request.trackingNumber?.let { delivery.trackingNumber = it }

        return DeliveryResponse.from(delivery)
    }

    @Transactional
    fun deleteDelivery(id: Long) {
        if (!deliveryRepository.existsById(id)) {
            throw IllegalArgumentException("Delivery not found")
        }
        deliveryRepository.deleteById(id)
    }
}
