package de.hsworms.android.modernwalkingapp.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.material.Scaffold
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.presentation.*
import de.hsworms.android.modernwalkingapp.presentation.components.NavItem


@Composable
fun MainScreen(profileViewModel: ProfileViewModel = viewModel()) {
    val navController = rememberNavController()
    val profiles = profileViewModel.profiles.observeAsState().value
    if (!profiles.isNullOrEmpty()) {
        Scaffold(
            topBar = { TopAppBar(navController) },
            bottomBar = { BottomNavigationBar(navController) },

            ) {
            Navigation(navController)
        }
    } else {
        RegisterScreen(navController = navController)
    }
}

@Composable
fun TopAppBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    if (currentRoute.toString() != "Walking"){
        SmallTopAppBar(
            title = {
                if (currentRoute.toString() == "start") {
                    Text(text = stringResource(R.string.modern_walking))
                } else {
                    Text(currentRoute.toString())
                }
            },
            navigationIcon = {
            },
            actions = {
            }
        )
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    val items = listOf(NavItem.Weather, NavItem.Statistic, NavItem.Start, NavItem.History, NavItem.Profile)

    if (currentRoute.toString() != "Walking"){
        NavigationBar(modifier = Modifier) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = { Icon(item.icon, contentDescription = null) },
                    //label = { Text(item.label) },
                    alwaysShowLabel = true,
                    selected = currentRoute == item.route,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            // Avoid multiple copies of the same destination when
                            // reselect the same item
                            launchSingleTop = true
                            // Restore state when selecting a previously selected item
                            restoreState = false
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    val startViewModel: StartViewModel = viewModel()
    NavHost(navController, startDestination = NavItem.Start.route) {
        composable(NavItem.Start.route) {
            StartScreen(navController, startViewModel = startViewModel)
        }
        composable(NavItem.Statistic.route) {
            StatisticScreen()
        }
        composable(NavItem.History.route) {
            HistoryScreen()
        }
        composable(NavItem.Weather.route) {
            WeatherScreen()
        }
        composable(NavItem.Profile.route) {
            ProfileScreen(navController)
        }
        composable("Edit") {
            EditScreen(navController)
        }

        composable("Profil Erstellen") {
            RegisterScreen(navController)
        }
        composable("Über uns") {
            WebViewScreen()
        }
        composable("Walking") {
            WalkScreen(navController, startViewModel = startViewModel)
        }
    }
}


@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}