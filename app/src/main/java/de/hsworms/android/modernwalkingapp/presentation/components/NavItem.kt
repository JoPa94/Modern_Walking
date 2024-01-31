package de.hsworms.android.modernwalkingapp.presentation.components
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavItem(
    var route: String,
    val icon : ImageVector,
    val label : String
) {
    object Weather : NavItem("Wetter", Icons.Filled.Cloud, "Wetter")
    object Statistic : NavItem("Fortschritte", Icons.Filled.BarChart, "Statistic")
    object Start : NavItem("start", Icons.Filled.Bolt, "Start")
    object History : NavItem("Verlauf", Icons.Filled.History, "History")
    object Profile : NavItem("Profil", Icons.Filled.ManageAccounts, "Profile")
}