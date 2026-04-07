package com.snanoh.e_commerce.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<ProductEntity, Long> {
    fun existsByCategoryCategoryId(categoryId: Long): Boolean
}
