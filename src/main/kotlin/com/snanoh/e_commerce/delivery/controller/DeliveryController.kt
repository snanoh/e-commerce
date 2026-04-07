package com.snanoh.e_commerce.delivery.controller

import com.snanoh.e_commerce.delivery.dto.DeliveryCreateRequest
import com.snanoh.e_commerce.delivery.dto.DeliveryResponse
import com.snanoh.e_commerce.delivery.dto.DeliveryUpdateRequest
import com.snanoh.e_commerce.delivery.service.DeliveryService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/deliveries")
class DeliveryController(
    private val deliveryService: DeliveryService
) {
    @GetMapping
    fun getAllDeliveries(): ResponseEntity<List<DeliveryResponse>> {
        return ResponseEntity.ok(deliveryService.getAllDeliveries())
    }

    @GetMapping("/{id}")
    fun getDeliveryById(@PathVariable id: Long): ResponseEntity<DeliveryResponse> {
        return try {
            ResponseEntity.ok(deliveryService.getDeliveryById(id))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping
    fun createDelivery(@RequestBody request: DeliveryCreateRequest): ResponseEntity<Any> {
        return try {
            val response = deliveryService.createDelivery(request)
            ResponseEntity.status(HttpStatus.CREATED).body(response)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @PutMapping("/{id}")
    fun updateDelivery(
        @PathVariable id: Long,
        @RequestBody request: DeliveryUpdateRequest
    ): ResponseEntity<Any> {
        return try {
            val response = deliveryService.updateDelivery(id, request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/{id}")
    fun deleteDelivery(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            deliveryService.deleteDelivery(id)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }
}
