package com.pemmob.reuse_pemmob.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ProductStatus {
    AVAILABLE,
    SOLD
}

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val price: Long,
    val condition: String,
    val description: String,
    val location: String,
    val imageUrl: String,
    val sellerName: String,
    val whatsapp: String,
    val status: String = ProductStatus.AVAILABLE.name
) {
    val statusEnum: ProductStatus
        get() = try {
            ProductStatus.valueOf(status)
        } catch (e: Exception) {
            ProductStatus.AVAILABLE
        }
}
