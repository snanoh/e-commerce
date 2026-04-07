package com.snanoh.e_commerce.category.dto

import com.snanoh.e_commerce.category.CategoryEntity

data class CategoryCreateRequest(
    val name: String
)

data class CategoryUpdateRequest(
    val name: String
)

data class CategoryResponse(
    val categoryId: Long,
    val name: String
) {
    companion object {
        fun from(category: CategoryEntity): CategoryResponse {
            return CategoryResponse(
                categoryId = category.categoryId,
                name = category.name
            )
        }
    }
}
