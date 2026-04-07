package com.snanoh.e_commerce.product.service

import com.snanoh.e_commerce.product.ProductEntity
import com.snanoh.e_commerce.product.ProductRepository
import com.snanoh.e_commerce.product.dto.ProductCreateRequest
import com.snanoh.e_commerce.product.dto.ProductResponse
import com.snanoh.e_commerce.product.dto.ProductUpdateRequest
import com.snanoh.e_commerce.category.CategoryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) {
    @Transactional(readOnly = true)
    fun getAllProducts(): List<ProductResponse> {
        return productRepository.findAll().map { ProductResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getProductById(id: Long): ProductResponse {
        val product = productRepository.findById(id).orElseThrow { IllegalArgumentException("Product not found") }
        return ProductResponse.from(product)
    }

    @Transactional
    fun createProduct(request: ProductCreateRequest): ProductResponse {
        val category = categoryRepository.findById(request.categoryId).orElseThrow { IllegalArgumentException("Category not found") }
        val now = Instant.now()
        val product = ProductEntity(
            name = request.name,
            createDate = now,
            createId = request.createId,
            modifyDate = now,
            price = request.price,
            stockQuantity = request.stockQuantity,
            category = category,
            imageUrls = request.imageUrls.toMutableList()
        )
        val saved = productRepository.save(product)
        return ProductResponse.from(saved)
    }

    @Transactional
    fun updateProduct(id: Long, request: ProductUpdateRequest): ProductResponse {
        val product = productRepository.findById(id).orElseThrow { IllegalArgumentException("Product not found") }
        
        request.name?.let { product.name = it }
        request.price?.let { product.price = it }
        request.stockQuantity?.let { product.stockQuantity = it }
        request.categoryId?.let {
            val category = categoryRepository.findById(it).orElseThrow { IllegalArgumentException("Category not found") }
            product.category = category
        }
        request.imageUrls?.let {
            product.imageUrls.clear()
            product.imageUrls.addAll(it)
        }
        product.modifyDate = Instant.now()
        
        return ProductResponse.from(product)
    }

    @Transactional
    fun deleteProduct(id: Long) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("Product not found")
        }
    }
}
