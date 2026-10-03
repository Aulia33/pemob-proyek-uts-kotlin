package com.pemmob.reuse_pemmob.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.pemmob.reuse_pemmob.data.local.AppDatabase
import com.pemmob.reuse_pemmob.data.model.Product
import com.pemmob.reuse_pemmob.data.model.ProductStatus
import com.pemmob.reuse_pemmob.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository

    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("Semua")
    val myItemsStatusFilter = MutableStateFlow(ProductStatus.AVAILABLE)

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = ProductRepository(database.productDao())
    }

    val allProducts: StateFlow<List<Product>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredHomeProducts: StateFlow<List<Product>> = combine(
        allProducts,
        searchQuery,
        selectedCategory
    ) { products, query, category ->
        products.filter { product ->
            val matchesSearch = query.isBlank() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.location.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)

            val matchesCategory = category == "Semua" || product.category.equals(category, ignoreCase = true)

            matchesSearch && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val myItemsProducts: StateFlow<List<Product>> = combine(
        allProducts,
        myItemsStatusFilter
    ) { products, status ->
        products.filter { it.statusEnum == status }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getProductById(id: Int): Flow<Product?> {
        return repository.getProductById(id)
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
    }

    fun onCategorySelect(category: String) {
        selectedCategory.value = category
    }

    fun onMyItemsStatusFilterChange(status: ProductStatus) {
        myItemsStatusFilter.value = status
    }

    fun publishProduct(
        name: String,
        category: String,
        priceString: String,
        condition: String,
        description: String,
        location: String,
        whatsapp: String,
        imageUrl: String,
        sellerName: String = "Saya",
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val price = priceString.toLongOrNull() ?: 0L
            val product = Product(
                name = name.trim(),
                category = category.trim(),
                price = price,
                condition = condition.trim(),
                description = description.trim(),
                location = location.trim(),
                imageUrl = imageUrl.ifBlank { "android.resource://${getApplication<Application>().packageName}/drawable/product_converse" },
                sellerName = sellerName,
                whatsapp = whatsapp.trim(),
                status = ProductStatus.AVAILABLE.name
            )
            repository.insertProduct(product)
            showSnackbar("✓ Barang berhasil dipublikasikan")
            onSuccess()
        }
    }

    fun updateProduct(
        product: Product,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            repository.updateProduct(product)
            showSnackbar("✓ Barang berhasil diperbarui")
            onSuccess()
        }
    }

    fun toggleProductStatus(product: Product) {
        viewModelScope.launch {
            val newStatus = if (product.statusEnum == ProductStatus.AVAILABLE) ProductStatus.SOLD else ProductStatus.AVAILABLE
            repository.updateProductStatus(product.id, newStatus)
            val msg = if (newStatus == ProductStatus.SOLD) "✓ Barang ditandai sebagai terjual" else "✓ Barang tersedia kembali"
            showSnackbar(msg)
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            showSnackbar("✓ Barang berhasil dihapus")
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbarMessage() {
        _snackbarMessage.value = null
    }
}
