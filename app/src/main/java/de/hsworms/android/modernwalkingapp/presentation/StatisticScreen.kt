package de.hsworms.android.modernwalkingapp.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.domain.model.History
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation

@SuppressLint("UnrememberedMutableState")
@Composable
fun StatisticScreen(historyViewModel: HistoryViewModel = viewModel()){
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val histories = historyViewModel.histories.observeAsState()
    val recentHistories = histories.value?.takeLast(7)?.reversed()

    val optionsList = mutableListOf("Distanz", "Zeit", "Kalorien", "Schritte")
    var expanded by remember { mutableStateOf(false) }
    var listItem: String by remember { mutableStateOf(optionsList[0]) }
    val startingType = remember { mutableStateOf("km")}


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround


    ){
        Column( horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                Modifier.padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = listItem, fontSize = 28.sp, modifier = Modifier.padding(end = 8.dp))
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }
            }
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.background),
                onDismissRequest = { expanded = false }
            ) {
                optionsList.forEach{ item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            expanded = false
                            listItem = item
                            when (item) {
                                "Distanz" -> {
                                    startingType.value = "km"
                                }
                                "Zeit" -> {
                                    startingType.value = "min"
                                }
                                "Kalorien" -> {
                                    startingType.value = "cal"
                                }
                                else -> {
                                    startingType.value = "Schritte"
                                }
                            }
                        },

                        )
                }
            }
        }
        AverageCard(modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            //.align(Alignment.CenterHorizontally)
            .padding(horizontal = 10.dp), recentHistories, startingType.value)
        Spacer(modifier = Modifier.weight(1f))

        when (startingType.value) {
            "cal" -> {
                HistoryBarChart(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(8f),
                    recentHistories, "cal")
            }
            "min" -> {
                HistoryBarChart(modifier = Modifier
                    .fillMaxSize()
                    .weight(8f), recentHistories, "time")
            }
            "km" -> {
                HistoryBarChart(modifier = Modifier
                    .fillMaxSize()
                    .weight(8f), recentHistories, "km")
            }
            else -> {
                HistoryBarChart(modifier = Modifier
                    .fillMaxSize()
                    .weight(8f), recentHistories, "steps")
            }
        }
        Spacer(modifier = Modifier.weight((2.1f)))
    }
}

@Composable
fun AverageCard(modifier: Modifier, histList: List<History>?, walktype: String) {
    var title = "Ø"
    val unit: String
    var avg = 0f
    when (walktype) {
        "cal" -> {
            histList?.forEachIndexed { _, history ->
                avg += history.calBurned?.toFloat() ?: 0f
            }
            title += stringResource(R.string.calories_burned)
            unit = stringResource(R.string.cal_unit)
        }
        "min" -> {
            histList?.forEachIndexed { _, history ->
                avg += history.minAchieved?.toFloat() ?: 0f
            }
            title += stringResource(R.string.minutes_walked)
            unit = stringResource(R.string.min_unit)
        }
        "km" -> {
            histList?.forEachIndexed { _, history ->
                avg += history.kmAchieved ?: 0f
            }
            title += stringResource(R.string.distance_walked)
            unit = stringResource(R.string.km_unit)
        }
        else -> {
            histList?.forEachIndexed { _, history ->
                avg += history.stepsAchieved?.toFloat() ?: 0f
            }
            title += stringResource(R.string.steps_walked)
            unit = stringResource(R.string.steps_unit)
        }
    }

    avg /= (histList?.size ?: 0)

    RoundedCard(modifier = modifier) {
        Column {
            Text(
                text = title,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 25.sp)
            )
            Text(
                text = avg.toString() + unit,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .align(Alignment.CenterHorizontally),
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun HistoryBarChart(modifier: Modifier, barList: List<History>?, walktype: String) {
    val orange = MaterialTheme.colorScheme.primary

    BarChart(
        barChartData = BarChartData(
            bars = getBarList(barList, walktype),
        ),
        // Optional properties.
        modifier = modifier,
        animation = simpleChartAnimation(),
        barDrawer = SimpleBarDrawer(),
        xAxisDrawer = SimpleXAxisDrawer(axisLineColor = orange),
        yAxisDrawer = SimpleYAxisDrawer(labelTextColor = orange, axisLineColor = orange),
        labelDrawer = SimpleLabelDrawer(labelTextColor = MaterialTheme.colorScheme.onPrimaryContainer, labelTextSize = 15.sp)
    )
}

@Composable
fun getBarList(barList: List<History>?, walktype: String): MutableList<BarChartData.Bar> {
    val orange = MaterialTheme.colorScheme.primaryContainer
    val finaleList: MutableList<BarChartData.Bar> = mutableListOf()
    finaleList.add(
        BarChartData.Bar(
            label = "ERROR",
            value = 200f,
            color = Color.Red
        )
    )

    if (walktype == "time") {
        if (!barList.isNullOrEmpty()) {
            barList.forEachIndexed { _, history ->
                finaleList.add(
                    BarChartData.Bar(
                        label = formatDate(history.startTime).toString(),
                        value = history.minAchieved?.toFloat() ?: 0f,
                        color = orange
                    )
                )
            }
            finaleList.removeAt(0)
        }
    } else if (walktype == "cal") {
        if (!barList.isNullOrEmpty()) {
            barList.forEachIndexed { _, history ->
                finaleList.add(
                    BarChartData.Bar(
                        label = formatDate(history.startTime).toString(),
                        value = history.calBurned?.toFloat() ?: 0f,
                        color = orange
                    )
                )
            }
            finaleList.removeAt(0)
        }
    } else if (walktype == "km") {
        if (!barList.isNullOrEmpty()) {
            barList.forEachIndexed { _, history ->
                finaleList.add(
                    BarChartData.Bar(
                        label = formatDate(history.startTime).toString(),
                        value = history.kmAchieved ?: 0f,
                        color = orange
                    )
                )
            }
            finaleList.removeAt(0)
        }
    } else {
        if (!barList.isNullOrEmpty()) {
            barList.forEachIndexed { _, history ->
                finaleList.add(
                    BarChartData.Bar(
                        label = formatDate(history.startTime).toString(),
                        value = history.stepsAchieved?.toFloat() ?: 0f,
                        color = orange
                    )
                )
            }
            finaleList.removeAt(0)
        }
    }
    return finaleList
}