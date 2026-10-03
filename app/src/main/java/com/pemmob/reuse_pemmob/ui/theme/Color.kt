package com.pemmob.reuse_pemmob.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Primary Brand Direction Colors
val DeepBlue = Color(0xFF1565C0)
val PrimaryBlue = Color(0xFF1976D2)
val LightBlue = Color(0xFF42A5F5)

// Material 3 Light Roles
val PrimaryLight = Color(0xFF1976D2)
val OnPrimaryLight = Color.White
val PrimaryContainerLight = Color(0xFFDCE3F9)
val OnPrimaryContainerLight = Color(0xFF001944)

val SecondaryLight = Color(0xFF555F71)
val OnSecondaryLight = Color.White
val SecondaryContainerLight = Color(0xFFD9E3F8)
val OnSecondaryContainerLight = Color(0xFF121C2B)

val SurfaceLight = Color(0xFFF8F9FF)
val OnSurfaceLight = Color(0xFF191C20)
val SurfaceVariantLight = Color(0xFFE0E2EC)
val OnSurfaceVariantLight = Color(0xFF43474E)
val OutlineLight = Color(0xFF74777F)

// Material 3 Dark Roles
val PrimaryDark = Color(0xFF9ECAFF)
val OnPrimaryDark = Color(0xFF00326B)
val PrimaryContainerDark = Color(0xFF004895)
val OnPrimaryContainerDark = Color(0xFFDCE3F9)

val SecondaryDark = Color(0xFFBDC7DC)
val OnSecondaryDark = Color(0xFF273141)
val SecondaryContainerDark = Color(0xFF3E4758)
val OnSecondaryContainerDark = Color(0xFFD9E3F8)

val SurfaceDark = Color(0xFF111318)
val OnSurfaceDark = Color(0xFFE2E2E9)
val SurfaceVariantDark = Color(0xFF43474E)
val OnSurfaceVariantDark = Color(0xFFC4C6D0)
val OutlineDark = Color(0xFF8E9099)

// Selective Blue Gradient Brush (for brand logo / header accent)
val BrandBlueGradient = Brush.horizontalGradient(
    colors = listOf(DeepBlue, PrimaryBlue, LightBlue)
)