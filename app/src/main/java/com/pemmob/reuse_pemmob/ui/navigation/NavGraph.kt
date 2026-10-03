package com.pemmob.reuse_pemmob.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemmob.reuse_pemmob.ui.screens.EditProductScreen
import com.pemmob.reuse_pemmob.ui.screens.HomeScreen
import com.pemmob.reuse_pemmob.ui.screens.MyItemsScreen
import com.pemmob.reuse_pemmob.ui.screens.ProductDetailScreen
import com.pemmob.reuse_pemmob.ui.screens.SellScreen
import com.pemmob.reuse_pemmob.ui.viewmodel.ProductViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector?) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Sell : Screen("sell", "Sell", Icons.Filled.Add)
    object MyItems : Screen("my_items", "My Items", Icons.Filled.Inventory)
    object ProductDetail : Screen("product_detail/{productId}", "Detail", null) {
        fun createRoute(productId: Int) = "product_detail/$productId"
    }
    object EditProduct : Screen("edit_product/{productId}", "Edit", null) {
        fun createRoute(productId: Int) = "edit_product/$productId"
    }
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Sell,
    Screen.MyItems
)

@Composable
fun MainAppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: ProductViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearSnackbarMessage()
        }
    }

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Sell.route,
        Screen.MyItems.route
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    bottomNavItems.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                if (currentRoute != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            },
                            icon = {
                                item.icon?.let { icon ->
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = item.title
                                    )
                                }
                            },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    }
                )
            }

            composable(Screen.Sell.route) {
                SellScreen(
                    viewModel = viewModel,
                    onPublishSuccess = {
                        navController.navigate(Screen.MyItems.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Screen.MyItems.route) {
                MyItemsScreen(
                    viewModel = viewModel,
                    onEditClick = { productId ->
                        navController.navigate(Screen.EditProduct.createRoute(productId))
                    },
                    onProductClick = { productId ->
                        navController.navigate(Screen.ProductDetail.createRoute(productId))
                    },
                    onNavigateToSell = {
                        navController.navigate(Screen.Sell.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = Screen.ProductDetail.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                ProductDetailScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.EditProduct.route,
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: 0
                EditProductScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
