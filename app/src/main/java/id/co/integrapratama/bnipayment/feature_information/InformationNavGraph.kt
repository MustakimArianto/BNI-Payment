package id.co.integrapratama.bnipayment.feature_information

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import id.co.integrapratama.bnipayment.navigation.AppRoute

fun NavGraphBuilder.informationNavGraph(navController: NavController) {
    navigation<AppRoute.Information>(
        startDestination = InformationRoute.Menu
    ) {
        composable<InformationRoute.Menu> {
            InformationScreen()
        }
    }
}