package com.pemmob.reuse_pemmob.ui.screens

import android.net.Uri
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.pemmob.reuse_pemmob.ui.components.CATEGORIES
import com.pemmob.reuse_pemmob.ui.viewmodel.ProductViewModel

val CONDITIONS = listOf("Baik", "Sangat Baik", "Seperti Baru")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellScreen(
    viewModel: ProductViewModel,
    onPublishSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf(CATEGORIES.filter { it != "Semua" }.first()) }
    var price by remember { mutableStateOf("") }
    var condition by remember { mutableStateOf("Baik") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

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
            imageUri = uri
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Tambah Barang",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "Foto Barang",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AddAPhoto,
                        contentDescription = "Pilih Foto",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Pilih Foto Barang",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CONDITIONS.forEach { cond ->
                val isSelected = condition == cond
                FilterChip(
                    selected = isSelected,
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
            label = { Text("Nomor WhatsApp (misal: 08123456789)") },
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

                if (name.isBlank()) nameError = "Nama barang wajib diisi"
                if ((price.toLongOrNull() ?: 0L) <= 0) priceError = "Harga harus lebih dari 0"
                if (description.isBlank()) descriptionError = "Deskripsi wajib diisi"
                if (location.isBlank()) locationError = "Lokasi wajib diisi"
                if (whatsapp.isBlank() || whatsapp.length < 8) whatsappError = "Nomor WhatsApp tidak valid"

                if (!hasError) {
                    viewModel.publishProduct(
                        name = name,
                        category = category,
                        priceString = price,
                        condition = condition,
                        description = description,
                        location = location,
                        whatsapp = whatsapp,
                        imageUrl = imageUri?.toString() ?: "",
                        sellerName = "Saya",
                        onSuccess = {
                            name = ""
                            price = ""
                            description = ""
                            location = ""
                            whatsapp = ""
                            imageUri = null
                            onPublishSuccess()
                        }
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Publikasikan Barang",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
