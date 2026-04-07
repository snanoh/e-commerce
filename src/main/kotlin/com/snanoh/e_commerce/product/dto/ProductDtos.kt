package com.snanoh.e_commerce.product.dto

import com.snanoh.e_commerce.product.ProductEntity
import java.time.Instant

data class ProductCreateRequest(
    val name: String,
    val createId: String,
    val price: Long,
    val stockQuantity: Int,
    val categoryId: Long,
    val imageUrls: List<String> = emptyList()
)

data class ProductUpdateRequest(
    val name: String?,
    val price: Long?,
    val stockQuantity: Int?,
    val categoryId: Long?,
    val imageUrls: List<String>? = null
)

data class ProductResponse(
    val productId: Long,
    val name: String,
    val createDate: Instant,
    val createId: String,
    val modifyDate: Instant,
    val price: Long,
    val stockQuantity: Int,
    val categoryId: Long?,
    val categoryName: String?,
    val imageUrls: List<String>
) {
    companion object {
        fun from(product: ProductEntity): ProductResponse {
            return ProductResponse(
                productId = product.productId,
                name = product.name,
                createDate = product.createDate,
                createId = product.createId,
                modifyDate = product.modifyDate,
                price = product.price,
                stockQuantity = product.stockQuantity,
                categoryId = product.category?.categoryId,
                categoryName = product.category?.name,
                imageUrls = product.imageUrls.toList()
            )
        }
    }
}
