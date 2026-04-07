package com.snanoh.e_commerce.category.service

import com.snanoh.e_commerce.category.CategoryEntity
import com.snanoh.e_commerce.category.CategoryRepository
import com.snanoh.e_commerce.category.dto.CategoryCreateRequest
import com.snanoh.e_commerce.category.dto.CategoryResponse
import com.snanoh.e_commerce.category.dto.CategoryUpdateRequest
import com.snanoh.e_commerce.product.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val productRepository: ProductRepository
) {
    @Transactional(readOnly = true)
    fun getAllCategories(): List<CategoryResponse> {
        return categoryRepository.findAll().map { CategoryResponse.from(it) }
    }

    @Transactional(readOnly = true)
    fun getCategoryById(id: Long): CategoryResponse {
        val category = categoryRepository.findById(id).orElseThrow { IllegalArgumentException("Category not found") }
        return CategoryResponse.from(category)
    }

    @Transactional
    fun createCategory(request: CategoryCreateRequest): CategoryResponse {
        val category = CategoryEntity(name = request.name)
        val saved = categoryRepository.save(category)
        return CategoryResponse.from(saved)
    }

    @Transactional
    fun updateCategory(id: Long, request: CategoryUpdateRequest): CategoryResponse {
        val category = categoryRepository.findById(id).orElseThrow { IllegalArgumentException("Category not found") }
        category.name = request.name
        return CategoryResponse.from(category)
    }

    @Transactional
    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id).orElseThrow { IllegalArgumentException("Category not found") }
        
        if (productRepository.existsByCategoryCategoryId(id)) {
            throw IllegalArgumentException("Cannot delete category because there are products associated with it.")
        }
        
        categoryRepository.delete(category)
    }
}
