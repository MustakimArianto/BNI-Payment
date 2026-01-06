// MainMenuScreen.kt
package id.co.integrapratama.bnipayment.feature_menu

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.co.integrapratama.bnipayment.feature_account.AccountScreen
import id.co.integrapratama.bnipayment.feature_admin.AdminScreen
import id.co.integrapratama.bnipayment.feature_admin.adminNavGraph
import id.co.integrapratama.bnipayment.feature_home.HomeMenuScreen
import id.co.integrapratama.bnipayment.feature_information.InformationScreen
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.NavBlue
import id.co.integrapratama.bnipayment.ui.theme.YellowBase

@Composable
internal fun MainMenuScreen(
    onNavigateToMiniATM: () -> Unit,
    onNavigateToSale: () -> Unit,
    onNavigateToContactlessSale: () -> Unit,
    onNavigateToVoid: () -> Unit,
    onNavigateToSettlement: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination?.route,
                onNavigate = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(BottomNavItem.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = BottomNavItem.HOME.route
            ) {
                composable<AppRoute.Home> {
                    HomeMenuScreen(
                        onNavigateToMiniATM = onNavigateToMiniATM,
                        onNavigateToSale = onNavigateToSale,
                        onNavigateToContactlessSale = onNavigateToContactlessSale,
                        onNavigateToVoid = onNavigateToVoid,
                        onNavigateToSettlement = onNavigateToSettlement
                    )
                }

                composable<AppRoute.Information> {
                    InformationScreen()
                }

                adminNavGraph(bottomNavController)

                composable<AppRoute.Account> {
                    AccountScreen()
                }
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    currentDestination: String?,
    onNavigate: (AppRoute) -> Unit
) {
    NavigationBar(containerColor = NavBlue) {
        BottomNavItem.entries.forEach { item ->
            val isSelected = currentDestination == item.route::class.qualifiedName

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = YellowBase,
                    selectedTextColor = Color.White,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = Color.White,
                    unselectedTextColor = Color.White
                )
            )
        }
    }
}