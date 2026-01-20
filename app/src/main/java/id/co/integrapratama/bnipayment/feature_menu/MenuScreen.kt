package id.co.integrapratama.bnipayment.feature_menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import id.co.integrapratama.bnipayment.feature_admin.adminNavigation
import id.co.integrapratama.bnipayment.feature_home.homeNavigation
import id.co.integrapratama.bnipayment.feature_init_menu.initMenuNavigation
import id.co.integrapratama.bnipayment.navigation.AppRoute
import id.co.integrapratama.bnipayment.ui.theme.InactiveColor
import id.co.integrapratama.bnipayment.ui.theme.LightGray
import id.co.integrapratama.bnipayment.ui.theme.SecondaryColor

@Composable
internal fun MenuScreen(
    isCheckDefaultMenu: Boolean,
    isHomeActive: Boolean,
    onDisableMenuChecking: () -> Unit
) {
    val bottomNavController = rememberNavController()
    val currentBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    val shouldShowBottomBar = when {
        currentDestination?.route?.contains("HomeRoute.Menu") == true -> true
        currentDestination?.route?.contains("InitMenuRoute.Menu") == true -> true
        else -> false
    }

    // Determine start destination based on conditions
    val startDestination = if (isCheckDefaultMenu) {
        if (isHomeActive) {
            BottomNavItem.HOME.route
        } else {
            BottomNavItem.INIT_MENU.route
        }
    } else {
        BottomNavItem.HOME.route
    }

    // Disable menu checking when we successfully reach Home screen
    LaunchedEffect(currentDestination?.route, isCheckDefaultMenu) {
        if (isCheckDefaultMenu && currentDestination?.route?.contains("HomeRoute") == true) {
            onDisableMenuChecking()
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                MainBottomBar(
                    isHomeActive = isHomeActive,
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
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = bottomNavController,
                startDestination = startDestination
            ) {
                homeNavigation(bottomNavController)
                adminNavigation(bottomNavController)
                initMenuNavigation(bottomNavController)
            }
        }
    }
}

@Composable
private fun MainBottomBar(
    isHomeActive: Boolean,
    currentDestination: String?,
    onNavigate: (AppRoute) -> Unit
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth()) {
            BottomNavItem.entries.forEach { item ->
                val isSelected = when (item) {
                    BottomNavItem.HOME -> currentDestination?.contains("HomeRoute") == true
                    BottomNavItem.ADMIN_SETTING -> currentDestination?.contains("AdminRoute") == true
                    BottomNavItem.INIT_MENU -> currentDestination?.contains("InitMenuRoute") == true
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(3.dp)
                        .background(if (isSelected) SecondaryColor else LightGray)
                )
            }
        }

        NavigationBar(containerColor = Color.White) {
            BottomNavItem.entries.forEach { item ->
                val enabled = when (item) {
                    BottomNavItem.HOME -> isHomeActive
                    else -> true
                }

                val isSelected = when (item) {
                    BottomNavItem.HOME -> currentDestination?.contains("HomeRoute") == true
                    BottomNavItem.ADMIN_SETTING -> currentDestination?.contains("AdminRoute") == true
                    BottomNavItem.INIT_MENU -> currentDestination?.contains("InitMenuRoute") == true
                }

                NavigationBarItem(
                    selected = isSelected,
                    enabled = enabled,
                    onClick = {
                        if (enabled) {
                            onNavigate(item.route)
                        }
                    },
                    label = { Text(item.title) },
                    icon = {
                        Icon(
                            painter = painterResource(item.icon),
                            contentDescription = item.title
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = SecondaryColor,
                        selectedTextColor = Color.Black,
                        indicatorColor = Color.Transparent,
                        unselectedIconColor = if (enabled) InactiveColor else InactiveColor.copy(
                            alpha = 0.4f
                        ),
                        unselectedTextColor = if (enabled) InactiveColor else InactiveColor.copy(
                            alpha = 0.4f
                        ),
                        disabledIconColor = InactiveColor.copy(alpha = 0.4f),
                        disabledTextColor = InactiveColor.copy(alpha = 0.4f)
                    )
                )
            }
        }
    }
}