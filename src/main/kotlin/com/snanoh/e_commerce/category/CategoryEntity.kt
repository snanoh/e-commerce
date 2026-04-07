package com.snanoh.e_commerce.category

import jakarta.persistence.*

@Entity
@Table(name = "categories")
class CategoryEntity(
    @Column(nullable = false, unique = true)
    var name: String,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val categoryId: Long = 0L
)
