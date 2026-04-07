package com.snanoh.e_commerce.cart.controller

import com.snanoh.e_commerce.cart.dto.CartAddItemRequest
import com.snanoh.e_commerce.cart.dto.CartItemResponse
import com.snanoh.e_commerce.cart.dto.CartUpdateItemRequest
import com.snanoh.e_commerce.cart.service.CartService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/carts")
class CartController(
    private val cartService: CartService
) {
    @PostMapping
    fun addCartItem(@RequestBody request: CartAddItemRequest): ResponseEntity<Any> {
        return try {
            val response = cartService.addCartItem(request)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/users/{userId}")
    fun getCartItems(@PathVariable userId: Long): ResponseEntity<List<CartItemResponse>> {
        return ResponseEntity.ok(cartService.getCartItems(userId))
    }

    @PutMapping("/items/{cartItemId}")
    fun updateCartItem(
        @PathVariable cartItemId: Long,
        @RequestBody request: CartUpdateItemRequest
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(cartService.updateCartItem(cartItemId, request))
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }

    @DeleteMapping("/items/{cartItemId}")
    fun deleteCartItem(@PathVariable cartItemId: Long): ResponseEntity<Any> {
        return try {
            cartService.deleteCartItem(cartItemId)
            ResponseEntity.noContent().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(mapOf("error" to e.message))
        }
    }
}
