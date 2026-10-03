package com.pemmob.reuse_pemmob.data.repository

import com.pemmob.reuse_pemmob.data.local.ProductDao
import com.pemmob.reuse_pemmob.data.model.Product
import com.pemmob.reuse_pemmob.data.model.ProductStatus
import kotlinx.coroutines.flow.Flow

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    fun getProductsByStatus(status: ProductStatus): Flow<List<Product>> {
        return productDao.getProductsByStatus(status.name)
    }

    fun getProductById(id: Int): Flow<Product?> {
        return productDao.getProductById(id)
    }

    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteProductById(id: Int) {
        productDao.deleteProductById(id)
    }

    suspend fun updateProductStatus(id: Int, status: ProductStatus) {
        productDao.updateProductStatus(id, status.name)
    }
}
