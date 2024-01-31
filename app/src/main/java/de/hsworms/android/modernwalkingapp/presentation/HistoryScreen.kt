package de.hsworms.android.modernwalkingapp.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.hsworms.android.modernwalkingapp.domain.model.History
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = viewModel()){


    val histories = historyViewModel.histories.observeAsState()
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

// If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ){

        when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                CollapsableLazyRow(histories, Modifier)
            }
            else -> {
                CollapsableLazyColumn(histories, Modifier)
            }
        }

    }

}

@Composable
fun CollapsableLazyRow(
    histories: State<List<History>?>,
    modifier: Modifier = Modifier
) {
    val historySize = histories.value?.size ?: 0

    LazyColumn(modifier) {
        item() {
            for (i in 1..historySize step 2) {
                GenRows(index = i, histories = histories, historySize)
            }
            Spacer(modifier = Modifier.height(80.dp))
        }

    }
}

@Composable
fun GenRows(index: Int, histories: State<List<History>?>, historySize: Int) {
    val isEven = historySize % 2
    Row() {
        histories.value?.get(index-1)?.let {
            historyCard(historyItem = it, modifier = Modifier
                .padding(bottom = 8.dp)
                //.fillMaxWidth()
                .height(height = 250.dp)
                .weight(1f))
        }
        if (index != historySize) {
            Spacer(modifier = Modifier.width(20.dp))
            histories.value?.reversed()?.get(index)?.let {
                historyCard(
                    historyItem = it, modifier = Modifier
                        .padding(bottom = 8.dp)
                        //.fillMaxWidth()
                        .height(height = 250.dp)
                        .weight(1f)
                )
            }
        } else if (isEven != 1) {
            Spacer(modifier = Modifier.width(20.dp))
            histories.value?.reversed()?.get(index)?.let {
                historyCard(
                    historyItem = it, modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .height(height = 250.dp)
                        .weight(1f)
                )
            }
        } else {
            Spacer(modifier = Modifier.width(20.dp))
            Spacer(modifier = Modifier
                .padding(bottom = 8.dp)
                .weight(1f)
                .fillMaxWidth())
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun CollapsableLazyColumn(
    histories: State<List<History>?>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        histories.value?.reversed()?.forEachIndexed { i, historyItem ->
            item(key = "header_$i") {
                Row() {
                    historyCard(historyItem = historyItem, modifier = Modifier
                        .padding(bottom = 8.dp)
                        //.fillMaxWidth()
                        .height(height = 250.dp)
                        .weight(1f))
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        item() {
            Spacer(modifier = Modifier.height(60.dp))
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun historyCard(historyItem : History, modifier: Modifier) {
    Card(
        onClick = { /* Do something */ },
        modifier = modifier,
    ) {
        Row(modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = if (historyItem.walkType == "distance") "Laufen " + historyItem.goal + " km"
                else if (historyItem.walkType == "time") "Laufen " + historyItem.goal + " min"
                else "Verbrennen " + historyItem.goal + " cal",
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.Start,
                fontSize = 20.sp
            )

            Text(
                text = formatDate(historyItem.startTime).toString(),
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(10.dp),
                textAlign = TextAlign.End,
                fontSize = 20.sp
            )
        }

        Row(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier) {
                Text(
                    text = historyItem.stepsAchieved.toString(),
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
                    text = historyItem.minAchieved.toString(),
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
                    text = historyItem.calBurned.toString(),
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
                    text = "-",
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
                    text = "-",
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
                    text = historyItem.kmAchieved.toString(),
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

fun formatDate(date: Date?): String? {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val tempDate = date?.toInstant()?.atZone(ZoneId.of("UTC"))?.toLocalDate()
    return tempDate?.format(formatter)
}
