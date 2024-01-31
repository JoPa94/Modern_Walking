package de.hsworms.android.modernwalkingapp.presentation

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.domain.model.History
import de.hsworms.android.modernwalkingapp.presentation.components.NavItem
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation
import java.util.*

@Composable
fun StartScreen(navController: NavController, startViewModel: StartViewModel, historyViewModel: HistoryViewModel = viewModel(), walkViewModel: WalkViewModel = viewModel()){
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


    val histories = historyViewModel.histories.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ) {
        Column(
            modifier = Modifier.padding(top=20.dp, bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.home_your_goal),
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,

                )
            Row(
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .padding(bottom = 20.dp)
            ) {
                IconButton(
                    onClick = { startViewModel.decreaseGoalCounter() },
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .size(60.dp)
                        .border(4.dp, MaterialTheme.colorScheme.primary, shape = CircleShape),
                    //shape = RoundedCornerShape(50),
                    //border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    //modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Rounded.Remove,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "${startViewModel.goalCounter } ${startViewModel.startingType.value} ",
                    fontWeight = FontWeight.Bold,
                    //modifier = Modifier.align(Arr
                    textAlign = TextAlign.Center,
                    fontSize = 35.sp,

                    )

                IconButton(
                    onClick = { startViewModel.increaseGoalCounter() },
                    modifier = Modifier
                        .padding(start = 30.dp)
                        .size(60.dp)
                        .border(4.dp, MaterialTheme.colorScheme.primary, shape = CircleShape),
                    //shape = RoundedCornerShape(50),
                    //border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                    //modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Icon(Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier.size(50.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            val optionsList = mutableListOf<String>("Distanz", "Zeit", "Kalorien")
            var expanded by remember { mutableStateOf(false) }
            var listItem: String by remember { mutableStateOf(optionsList[0]) }

            Column( horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    Modifier.padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = CenterVertically
                ) {
                    Text(text = listItem, fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    onDismissRequest = { expanded = false }
                ) {
                    optionsList.forEach{ item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                expanded = false
                                listItem = item
                                if (item == "Distanz"){
                                    startViewModel.startingType.value = "km"
                                    startViewModel.walkType.value = "distance"
                                } else if (item =="Zeit"){
                                    startViewModel.startingType.value = "min"
                                    startViewModel.walkType.value = "time"
                                } else{
                                    startViewModel.startingType.value = "kal"
                                    startViewModel.walkType.value = "calories"
                                }
                            },

                            )
                    }
                }
            }

            val context = LocalContext.current
            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission Accepted: Do something
                    walkViewModel.lat = 49.6341
                    walkViewModel.lon = 8.3507
                    Toast.makeText(context, "Erlaubnis erteilt, jetzt kannst du mit dem ersten Lauf starten", Toast.LENGTH_LONG).show()

                } else {
                    // Permission Denied: Do something
                    walkViewModel.lat = 49.6341
                    walkViewModel.lon = 8.3507
                }
            }

            Button(
                onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) -> {
                            // Some works that require permission

                            walkViewModel.lat = 49.6341
                            walkViewModel.lon = 8.3507
                            requestLocationUpdates(walkViewModel)
                            navController.navigate("Walking")
                            walkViewModel.startDate = Date()

                        }
                        else -> {
                            // Asking for permission
                            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                            Toast.makeText(context, "Standort gewähren", Toast.LENGTH_LONG).show()
                        }
                    }
                     },
                contentPadding = PaddingValues(
                    start = 100.dp,
                    top = 12.dp,
                    end = 100.dp,
                    bottom = 12.dp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Start",
                    fontSize = 35.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .width(250.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "Verlauf",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
                fontSize = 25.sp,

                )

            SmallLazyColumn(
                navController,
                histories
            )
        }
    }
}


@Composable
fun SmallLazyColumn(
    navController:NavController,
    histories: State<List<History>?>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        val recentHistories = histories.value?.takeLast(3)?.reversed()
        recentHistories?.forEachIndexed { i, historyItem ->
            item(key = "header_$i") {
                historyItem(historyItem)

            }
        }
    }

    Row(
        Modifier.padding(0.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {
        Text(
            "Mehr anzeigen",
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f)
                .clickable {
                    navController.navigate(NavItem.History.route)
                },
            textAlign = TextAlign.End,

        )
    }
}

@Composable
fun historyItem(history: History) {
    var tintColor: Color
    var historyIcon: ImageVector
    if (history.walkType == "distance") {
        historyIcon = Icons.Filled.Place
        tintColor = MaterialTheme.colorScheme.primary
    } else if (history.walkType == "time") {
        historyIcon = Icons.Filled.Timer
        tintColor = Color.Green
    } else {
        historyIcon = Icons.Filled.LocalFireDepartment
        tintColor = Color.Yellow
    }
    Row(
        Modifier.padding(7.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = CenterVertically
    ) {

        Icon(
            Icons.Default.run {
                //Icons.Filled.LocalFireDepartment
                historyIcon
                //Icons.Filled.DirectionsRun
            },
            contentDescription = "",
            tint = tintColor,
            modifier = Modifier.padding(end = 5.dp)
        )
        Text(
            text = formatDate(history.startTime).toString(),
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f),
        )
        Text(
            text = when (history.walkType) {
                "distance" -> history.goal.toString() + " km"
                "time" -> history.goal.toString() + " min"
                else -> history.goal.toString() + " kal"
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(vertical = 10.dp)
                .weight(1f),
            textAlign = TextAlign.End
        )
    }
    Divider(
        color = MaterialTheme.colorScheme.primary,
        thickness = 1.5.dp
    )
}



/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ModernWalkingAppTheme {
        HomeScreen()
    }
}*/
private fun requestLocationUpdates(walkViewModel: WalkViewModel){
    walkViewModel.startLocationUpdates()
}
