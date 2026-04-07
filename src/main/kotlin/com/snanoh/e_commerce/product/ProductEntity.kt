package com.snanoh.e_commerce.product

import jakarta.persistence.*
import java.time.Instant


@Entity
@Table(name = "products")
class ProductEntity (
    var name: String,
    var createDate: Instant,
    var createId: String,
    var modifyDate: Instant,
    var price: Long,

    @Column(nullable = false)
    var stockQuantity: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    var category: com.snanoh.e_commerce.category.CategoryEntity? = null,

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_images", joinColumns = [JoinColumn(name = "product_id")])
    @Column(name = "image_url")
    var imageUrls: MutableList<String> = mutableListOf(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val productId: Long = 0L
) {
    fun removeStock(quantity: Int) {
        val restStock = this.stockQuantity - quantity
        if (restStock < 0) {
            throw IllegalArgumentException("Not enough stock")
        }
        this.stockQuantity = restStock
    }
}