package com.pemmob.reuse_pemmob.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pemmob.reuse_pemmob.data.model.Product
import com.pemmob.reuse_pemmob.data.model.ProductStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Product::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reuse_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance

                scope.launch(Dispatchers.IO) {
                    try {
                        if (instance.productDao().getCount() == 0) {
                            populateDatabase(context, instance.productDao())
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                instance
            }
        }

        suspend fun populateDatabase(context: Context, productDao: ProductDao) {
            val initialProducts = listOf(
                Product(
                    name = "Nike Air Jordan High OG",
                    category = "Sepatu",
                    price = 1250000,
                    condition = "Sangat Baik",
                    description = "Sepatu Nike Air Jordan High OG kondisi sangat terawat, pemakaian wajar, box masih ada.",
                    location = "Surabaya",
                    imageUrl = "product_nike_air_high",
                    sellerName = "Agastya",
                    whatsapp = "082326698593",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Converse Chuck Taylor All Star",
                    category = "Sepatu",
                    price = 350000,
                    condition = "Baik",
                    description = "Converse Chuck 70s hitam, size 42, sole masih tebal dan nyaman dipake harian.",
                    location = "Purwokerto",
                    imageUrl = "product_converse",
                    sellerName = "Diaz",
                    whatsapp = "081226648848",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Celana Jeans Levi's 501 Original",
                    category = "Fashion",
                    price = 280000,
                    condition = "Seperti Baru",
                    description = "Jeans Levi's 501 size 32, warna navy gelap, baru dipakai 2x dijual karena salah ukuran.",
                    location = "Purwokerto",
                    imageUrl = "product_levi_s",
                    sellerName = "Ratu",
                    whatsapp = "082272105386",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Hoodie Oversize Vintage",
                    category = "Fashion",
                    price = 120000,
                    condition = "Baik",
                    description = "Hoodie bahan fleece tebal warna washed grey, style streetwear boxy fit.",
                    location = "Bandung",
                    imageUrl = "product_hoodie",
                    sellerName = "Naila",
                    whatsapp = "08214395372",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "VGA Aorus RTX 5090 Extreme",
                    category = "Elektronik",
                    price = 25000000,
                    condition = "Sangat Baik",
                    description = "VGA Monster Aorus Extreme RTX 5090, kelengkapan fullset dus, garansi resmi aktif.",
                    location = "Semarang",
                    imageUrl = "product_aorus_5090",
                    sellerName = "Naira",
                    whatsapp = "081392854887",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Headphone Wireless Studio",
                    category = "Elektronik",
                    price = 250000,
                    condition = "Sangat Baik",
                    description = "Headphone Bluetooth bass mantap, batre tahan 20 jam, mulus seperti baru.",
                    location = "Jakarta",
                    imageUrl = "product_headphone",
                    sellerName = "Nayla",
                    whatsapp = "085788066564",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Tas Ransel Adidas Preloved",
                    category = "Tas",
                    price = 180000,
                    condition = "Baik",
                    description = "Backpack Adidas kapasitas 25L, muat laptop 15 inch, jahitan masih sangat kuat.",
                    location = "Purwokerto",
                    imageUrl = "product_adidas_backpack",
                    sellerName = "Agastya",
                    whatsapp = "082326698593",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Mechanical Keyboard RGB Custom",
                    category = "Gaming",
                    price = 420000,
                    condition = "Sangat Baik",
                    description = "Mechanical keyboard 75% hot-swappable yellow switch, suara thocky, RGB bright.",
                    location = "Yogyakarta",
                    imageUrl = "product_mechanical_keyboard",
                    sellerName = "Saski",
                    whatsapp = "085702281043",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "PlayStation 5 Digital Edition",
                    category = "Gaming",
                    price = 5400000,
                    condition = "Sangat Baik",
                    description = "PS5 Slim Digital edition, dapet 1 DualSense controller, kabel lengkap, mulus 98%.",
                    location = "Malang",
                    imageUrl = "product_playstation",
                    sellerName = "Javir",
                    whatsapp = "087832569328",
                    status = ProductStatus.AVAILABLE.name
                ),
                Product(
                    name = "Mouse Gaming Razer DeathAdder",
                    category = "Gaming",
                    price = 310000,
                    condition = "Baik",
                    description = "Mouse gaming ergonomic Razer, sensor presisi 20K DPI, klik masih empuk.",
                    location = "Solo",
                    imageUrl = "product_raze_mouse",
                    sellerName = "Aulia",
                    whatsapp = "083896200880",
                    status = ProductStatus.AVAILABLE.name
                )
            )

            productDao.insertAll(initialProducts)
        }
    }
}
