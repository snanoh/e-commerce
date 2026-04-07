package com.snanoh.e_commerce.cart.service

import com.snanoh.e_commerce.cart.domain.CartEntity
import com.snanoh.e_commerce.cart.domain.CartItemEntity
import com.snanoh.e_commerce.cart.domain.CartItemRepository
import com.snanoh.e_commerce.cart.domain.CartRepository
import com.snanoh.e_commerce.cart.dto.CartAddItemRequest
import com.snanoh.e_commerce.cart.dto.CartItemResponse
import com.snanoh.e_commerce.cart.dto.CartUpdateItemRequest
import com.snanoh.e_commerce.product.ProductRepository
import com.snanoh.e_commerce.user.domain.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartService(
    private val cartRepository: CartRepository,
    private val cartItemRepository: CartItemRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    @Transactional
    fun addCartItem(request: CartAddItemRequest): CartItemResponse {
        val user = userRepository.findById(request.userId).orElseThrow { IllegalArgumentException("User not found") }
        val product = productRepository.findById(request.productId).orElseThrow { IllegalArgumentException("Product not found") }

        var cart = cartRepository.findByUserUserId(user.userId)
        if (cart == null) {
            cart = cartRepository.save(CartEntity(user = user))
        }

        var cartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.cartId, product.productId)
        if (cartItem != null) {
            cartItem.count += request.count
        } else {
            cartItem = CartItemEntity(
                cart = cart,
                product = product,
                count = request.count
            )
        }
        val savedItem = cartItemRepository.save(cartItem)
        return CartItemResponse.from(savedItem)
    }

    @Transactional(readOnly = true)
    fun getCartItems(userId: Long): List<CartItemResponse> {
        val cart = cartRepository.findByUserUserId(userId) ?: return emptyList()
        return cartItemRepository.findByCartCartId(cart.cartId).map { CartItemResponse.from(it) }
    }

    @Transactional
    fun updateCartItem(cartItemId: Long, request: CartUpdateItemRequest): CartItemResponse {
        val cartItem = cartItemRepository.findById(cartItemId).orElseThrow { IllegalArgumentException("Cart Item not found") }
        cartItem.count = request.count
        return CartItemResponse.from(cartItem)
    }

    @Transactional
    fun deleteCartItem(cartItemId: Long) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw IllegalArgumentException("Cart Item not found")
        }
        cartItemRepository.deleteById(cartItemId)
    }
}
