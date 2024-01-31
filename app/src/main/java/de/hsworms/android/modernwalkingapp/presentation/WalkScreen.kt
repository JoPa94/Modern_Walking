package de.hsworms.android.modernwalkingapp.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.*
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.domain.model.History
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation
import java.text.DecimalFormat
import java.util.*


@SuppressLint("UnrememberedMutableState")
@Composable
fun WalkScreen(navController: NavController, historyViewModel: HistoryViewModel = viewModel(), walkViewModel: WalkViewModel = viewModel(), startViewModel: StartViewModel){
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)



    val location by walkViewModel.getLocationLiveData().observeAsState()



    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally

    ){


        GoogleMapView(  modifier = Modifier
            .height(420.dp)
            .padding(bottom = 20.dp),
            location,
            walkViewModel
        )

        Text(
            text = walkViewModel.lat.toString() + " / " + walkViewModel.lon.toString(),
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 5.dp),
            textAlign = TextAlign.Center,
            fontSize = 25.sp,
            )


        content(walkViewModel)
        stopButton(walkViewModel, startViewModel, historyViewModel, navController)

        LaunchedEffect(key1 = Unit, block = {
            try {
                walkViewModel.startTimer() { // start a timer for 5 secs
                    println("Timer ended")
                }
            } catch(ex: Exception) {
                println("timer cancelled")
            }
        })
    }
}


@Composable
fun content(walkViewModel: WalkViewModel) {

    WalkingCard(walkViewModel)
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkingCard(walkViewModel: WalkViewModel) {

    Card(
        onClick = { /* Do something */ },
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
            .height(height = 150.dp),
    ) {
        Row(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.stepsAchieved.toString(),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier,
                )
                Row(modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.RunCircle
                        },
                        contentDescription = "",
                        tint = Color.Magenta,
                    )
                    Text(
                        text = "  Schritte",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.minutes.toString() + ":" + walkViewModel.seconds,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center){
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.Timer

                        },
                        modifier = Modifier
                            .padding(end = 8.dp),
                        contentDescription = "",
                        tint = Color.Green,
                    )
                    Text(
                        text = "Dauer",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.calBurned.toString(),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.LocalFireDepartment
                        },
                        modifier = Modifier
                            .padding(end = 8.dp),
                        contentDescription = "",
                        tint = Color.Yellow,
                    )
                    Text(
                        text = "Cal",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }
        }


        // line 2
        Row(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.kmInMin,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier,
                )
                Row(modifier = Modifier.weight(1f)) {
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.Speed
                        },
                        contentDescription = "",
                        tint = Color.Cyan,
                    )
                    Text(
                        text = "  km/min",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.bmp,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center){
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.Favorite

                        },
                        modifier = Modifier
                            .padding(end = 8.dp),
                        contentDescription = "",
                        tint = Color.Red,
                    )
                    Text(
                        text = "bmp",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = walkViewModel.kmAchieved.toString(),
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                )
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End){
                    Icon(
                        Icons.Default.run {
                            Icons.Filled.Place
                        },
                        modifier = Modifier
                            .padding(end = 8.dp),
                        contentDescription = "",
                        tint =  MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "km",
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun stopButton(
    walkViewModel: WalkViewModel,
    startViewModel: StartViewModel,
    historyViewModel: HistoryViewModel,
    navController: NavController
) {
    Button(
        onClick = {

            var check = false
            if (startViewModel.walkType.value == "distance"){
                if (walkViewModel.kmAchieved > startViewModel.goalCounter){
                    check = true
            } else if (startViewModel.walkType.value == "time"){
                if ((walkViewModel.minAchieved / 60) > startViewModel.goalCounter){
                    check = true
                }
            } else{
                if (walkViewModel.calBurned < startViewModel.goalCounter){
                    check = true
                }
            }
            }

            val historyItem = History(walkType = startViewModel.walkType.value, walkTypeUnit = "min",
            goal = startViewModel.goalCounter, startTime = walkViewModel.startDate, endTime = Date(), minAchieved = walkViewModel.minAchieved/60,  stepsAchieved = walkViewModel.stepsAchieved, calBurned = walkViewModel.calBurned, kmAchieved = walkViewModel.kmAchieved, goalAchieved = check)
            historyViewModel.insertNewHistoryItem(historyItem)
            navController.navigate("start")

        },
        contentPadding = PaddingValues(
            start = 100.dp,
            top = 12.dp,
            end = 100.dp,
            bottom = 12.dp
        ),
    ) {
        Text(
            text = "Stop",
            fontSize = 35.sp
        )
    }
}

@Composable
fun GoogleMapView(
    modifier: Modifier,
    location: LocationDetails?,
    walkViewModel: WalkViewModel,
) {

    var defaultLocation = LocationDetails()


    if (location == null){
        walkViewModel.lat = defaultLocation.latitude
        walkViewModel.lon = defaultLocation.longitude
    }
    else{
        walkViewModel.lat = location.latitude
        walkViewModel.lon = location.longitude

        // last location
        val startPoint = Location("locationA")
        startPoint.latitude = walkViewModel.lastLat
        startPoint.longitude = walkViewModel.lastLon

        // current location
        val endPoint = Location("locationB")
        endPoint.latitude = walkViewModel.lat
        endPoint.longitude = walkViewModel.lon

        walkViewModel.distance = startPoint.distanceTo(endPoint)

        if (walkViewModel.distance > 3 && walkViewModel.distance < 20){
            walkViewModel.kmAchieved += DecimalFormat("#.##").format(startPoint.distanceTo(endPoint) / 1000).toFloat()
            walkViewModel.stepsAchieved += (walkViewModel.distance * 1.30).toInt()
            walkViewModel.calBurned += (walkViewModel.stepsAchieved * 0.045).toInt()
        }
        walkViewModel.lastLat = walkViewModel.lat
        walkViewModel.lastLon = walkViewModel.lon

    }




    // Observing and controlling the camera's state can be done with a CameraPositionState
    val cameraPositionState: CameraPositionState = rememberCameraPositionState()
    cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(walkViewModel.lat, walkViewModel.lon), 15f)

    val activity = LocalContext.current as Activity
    var uiSettings by remember { mutableStateOf(MapUiSettings(compassEnabled = false)) }
    var mapProperties by remember {
        mutableStateOf(
            MapProperties(mapType = MapType.NORMAL, mapStyleOptions = MapStyleOptions.loadRawResourceStyle(
            activity, R.raw.style_json))
        )
    }

        GoogleMap(
            modifier = modifier,
            cameraPositionState = cameraPositionState, // where are we going to start the map
            properties = mapProperties,
            uiSettings = uiSettings,
        ) {
            Marker(
                state = MarkerState(position = LatLng(walkViewModel.lat, walkViewModel.lon)),
                title = "Marker in Sydney"
            )


        }

}
