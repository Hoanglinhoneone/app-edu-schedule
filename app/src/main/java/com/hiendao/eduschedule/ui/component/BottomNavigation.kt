package com.hiendao.eduschedule.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.hiendao.eduschedule.ui.navigation.AppScreen
import timber.log.Timber

// TODO: Cần custom them
@Composable
fun HomeBottomNavigation(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route
    Timber.d("currentRoute : $currentRoute")
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Schedule,
        BottomNavItem.Notification,
        BottomNavItem.Setting
    )

    NavigationBar {
        items.forEach { item ->
            val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
//                label = { Text(text = item.title) },
                selected = selected,
                onClick = {
                    Timber.d("currentRoute : $currentRoute")
                    Timber.d("targetRoute : ${item.route}")
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object Home :
        BottomNavItem(AppScreen.HomeTab.name, Icons.Default.CalendarMonth, "Trang chủ")

    data object Schedule :
        BottomNavItem(AppScreen.Utilities.name, Icons.Default.Assignment, "Tiện ích")

    data object Notification :
        BottomNavItem(AppScreen.Notification.name, Icons.Default.Notifications, "Thông báo")

    data object Setting : BottomNavItem(AppScreen.Setting.name, Icons.Default.Settings, "Cài đặt")
}