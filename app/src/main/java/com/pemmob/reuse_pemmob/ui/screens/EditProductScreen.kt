package com.pemmob.reuse_pemmob.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pemmob.reuse_pemmob.ui.components.CATEGORIES
import com.pemmob.reuse_pemmob.ui.components.rememberImageModel
import com.pemmob.reuse_pemmob.ui.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    productId: Int,
    viewModel: ProductViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val productFlow = remember(productId) { viewModel.getProductById(productId) }
    val productState by productFlow.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Barang", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (productState == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val item = productState!!

            var name by remember(item) { mutableStateOf(item.name) }
            var category by remember(item) { mutableStateOf(item.category) }
            var price by remember(item) { mutableStateOf(item.price.toString()) }
            var condition by remember(item) { mutableStateOf(item.condition) }
            var description by remember(item) { mutableStateOf(item.description) }
            var location by remember(item) { mutableStateOf(item.location) }
            var whatsapp by remember(item) { mutableStateOf(item.whatsapp) }
            var imageUri by remember(item) { mutableStateOf(item.imageUrl) }

            var nameError by remember { mutableStateOf<String?>(null) }
            var priceError by remember { mutableStateOf<String?>(null) }
            var descriptionError by remember { mutableStateOf<String?>(null) }
            var locationError by remember { mutableStateOf<String?>(null) }
            var whatsappError by remember { mutableStateOf<String?>(null) }

            var categoryExpanded by remember { mutableStateOf(false) }

            val photoPickerLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    imageUri = uri.toString()
                }
            }

            val scrollState = rememberScrollState()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(rememberImageModel(imageUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto Barang",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = if (it.isBlank()) "Nama barang wajib diisi" else null
                    },
                    label = { Text("Nama Barang") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { },
                        readOnly = true,
                        label = { Text("Kategori") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        CATEGORIES.filter { it != "Semua" }.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = {
                                    category = cat
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = price,
                    onValueChange = {
                        price = it
                        val p = it.toLongOrNull()
                        priceError = if (p == null || p <= 0) "Harga harus lebih dari 0" else null
                    },
                    label = { Text("Harga (Rp)") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = priceError != null,
                    supportingText = priceError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Kondisi",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CONDITIONS.forEach { cond ->
                        FilterChip(
                            selected = condition == cond,
                            onClick = { condition = cond },
                            label = { Text(cond) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                        descriptionError = if (it.isBlank()) "Deskripsi wajib diisi" else null
                    },
                    label = { Text("Deskripsi") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    isError = descriptionError != null,
                    supportingText = descriptionError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = location,
                    onValueChange = {
                        location = it
                        locationError = if (it.isBlank()) "Lokasi wajib diisi" else null
                    },
                    label = { Text("Lokasi") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = locationError != null,
                    supportingText = locationError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = whatsapp,
                    onValueChange = {
                        whatsapp = it
                        whatsappError = if (it.isBlank() || it.length < 8) "Nomor WhatsApp tidak valid" else null
                    },
                    label = { Text("Nomor WhatsApp") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    isError = whatsappError != null,
                    supportingText = whatsappError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        val hasError = name.isBlank() ||
                                (price.toLongOrNull() ?: 0L) <= 0 ||
                                description.isBlank() ||
                                location.isBlank() ||
                                whatsapp.isBlank() || whatsapp.length < 8

                        if (!hasError) {
                            val updated = item.copy(
                                name = name.trim(),
                                category = category.trim(),
                                price = price.toLongOrNull() ?: item.price,
                                condition = condition,
                                description = description.trim(),
                                location = location.trim(),
                                whatsapp = whatsapp.trim(),
                                imageUrl = imageUri
                            )
                            viewModel.updateProduct(updated) {
                                onBackClick()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Simpan Perubahan",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
