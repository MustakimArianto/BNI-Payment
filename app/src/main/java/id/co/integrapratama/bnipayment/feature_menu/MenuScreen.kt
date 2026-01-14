// MenuScreen.kt
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
import id.co.integrapratama.bnipayment.feature_admin.adminNavigation
import id.co.integrapratama.bnipayment.feature_home.homeNavigation
import id.co.integrapratama.bnipayment.feature_init_menu.InitMenuScreen
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.InactiveColor
import id.co.integrapratama.bnipayment.ui.theme.SecondaryColor

@Composable
internal fun MenuScreen() {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            MainBottomBar(
                currentDestination = currentDestination?.route, onNavigate = { route ->
                    bottomNavController.navigate(route) {
                        popUpTo(BottomNavItem.HOME.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController, startDestination = BottomNavItem.HOME.route
            ) {
                homeNavigation(bottomNavController)

                adminNavigation(bottomNavController)

                composable<AppRoute.InitMenu> {
                    InitMenuScreen()
                }
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    currentDestination: String?, onNavigate: (AppRoute) -> Unit
) {
    NavigationBar(containerColor = Color.White) {
        BottomNavItem.entries.forEach { item ->
            val isSelected = when (item) {
                BottomNavItem.HOME -> currentDestination?.contains("HomeRoute") == true
                BottomNavItem.ADMIN_SETTING -> currentDestination?.contains("AdminRoute") == true
                BottomNavItem.INIT_MENU -> currentDestination == AppRoute.InitMenu::class.qualifiedName
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                label = { Text(text = item.title) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon), contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SecondaryColor,
                    selectedTextColor = Color.Black,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = InactiveColor,
                    unselectedTextColor = InactiveColor
                )
            )
        }
    }
}