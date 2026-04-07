package com.snanoh.e_commerce.cart.dto

import com.snanoh.e_commerce.cart.domain.CartItemEntity

data class CartAddItemRequest(
    val userId: Long,
    val productId: Long,
    val count: Int
)

data class CartUpdateItemRequest(
    val count: Int
)

data class CartItemResponse(
    val cartItemId: Long,
    val productId: Long,
    val productName: String,
    val count: Int,
    val price: Long
) {
    companion object {
        fun from(cartItem: CartItemEntity): CartItemResponse {
            return CartItemResponse(
                cartItemId = cartItem.cartItemId,
                productId = cartItem.product.productId,
                productName = cartItem.product.name,
                count = cartItem.count,
                price = cartItem.product.price
            )
        }
    }
}
