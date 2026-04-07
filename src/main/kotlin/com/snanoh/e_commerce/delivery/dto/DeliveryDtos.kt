package com.snanoh.e_commerce.delivery.dto

import com.snanoh.e_commerce.delivery.domain.DeliveryEntity
import com.snanoh.e_commerce.delivery.domain.DeliveryStatus

data class DeliveryCreateRequest(
    val recipientName: String,
    val recipientPhone: String,
    val address: String
)

data class DeliveryUpdateRequest(
    val recipientName: String?,
    val recipientPhone: String?,
    val address: String?,
    val status: DeliveryStatus?,
    val trackingNumber: String?
)

data class DeliveryResponse(
    val deliveryId: Long,
    val recipientName: String,
    val recipientPhone: String,
    val address: String,
    val status: DeliveryStatus,
    val trackingNumber: String?
) {
    companion object {
        fun from(entity: DeliveryEntity): DeliveryResponse {
            return DeliveryResponse(
                deliveryId = entity.deliveryId,
                recipientName = entity.recipientName,
                recipientPhone = entity.recipientPhone,
                address = entity.address,
                status = entity.status,
                trackingNumber = entity.trackingNumber
            )
        }
    }
}
