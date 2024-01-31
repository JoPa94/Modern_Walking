package de.hsworms.android.modernwalkingapp.ui

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.domain.model.WeatherModel




@Composable
fun WeatherScreen(){
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        WeatherMain()
    }

}

@Composable
fun WeatherMain() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = WeatherModel().date,
            style = MaterialTheme.typography.caption,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Row(modifier = Modifier.wrapContentSize(), verticalAlignment = Alignment.CenterVertically) {

            androidx.compose.material3.Icon(
                Icons.Default.run {
                    Icons.Filled.Place
                },
                contentDescription = "",
                tint = Color.LightGray,
            )
            Text(
                text = WeatherModel().location,
                style = MaterialTheme.typography.body2,
            )
        }
        // middle section
        Column(modifier = Modifier.fillMaxSize().background(androidx.compose.material3.MaterialTheme.colorScheme.background), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

                    ReusableImage(
                        image = R.drawable.ic_cloud_zappy,
                        contentScale = ContentScale.Fit,
                        contentDesc = "Weather img",
                        modifier = Modifier
                            .size(250.dp)
                            .padding(top = 40.dp, bottom = 20.dp)
                    )
                    Text(text = WeatherModel().condition, style = MaterialTheme.typography.subtitle1)
                    Spacer(modifier = Modifier.size(4.dp))
                    Row(modifier = Modifier.wrapContentSize()) {
                        Text(
                            text = WeatherModel().update.first().temp,
                            style = MaterialTheme.typography.h1,
                            modifier = Modifier.offset(y = (-24).dp)
                        )
                        ReusableImage(
                            image = R.drawable.ic_degree,
                            contentScale = ContentScale.Fit,
                            contentDesc = "Degree Icon",
                            modifier = Modifier
                        )
                    }
//                    Text(
//                        text = Weather().description,
//                        style = MaterialTheme.typography.body1,
//                        textAlign = TextAlign.Center
//                    )
                    Spacer(modifier = Modifier.size(16.dp))
            DailyWeatherList()
        }
    }
}

@Composable
fun ReusableImage(image: Int, contentScale: ContentScale, contentDesc: String, modifier: Modifier) {
    val paintImage: Painter = painterResource(id = image)
    Image(
        painter = paintImage,
        contentDescription = contentDesc,
        contentScale = contentScale,
        modifier = modifier
    )
}

@Composable
fun DailyWeatherList() {
    LazyRow(
        modifier = Modifier.background(androidx.compose.material3.MaterialTheme.colorScheme.background),
        content = {
            items(WeatherModel().update) { weather ->
                DailyWeatherItem(weather)
            }
        }
    )
}


@Composable
fun DailyWeatherItem(weatherModelUpdate: WeatherModel.WeatherUpdate) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.background)
        ) {
            ReusableImage(
                image = when (weatherModelUpdate.icon) {
                    "wind" -> R.drawable.ic_moon_cloud_fast_wind
                    "regen" -> R.drawable.ic_moon_cloud_mid_rain
                    "schauer" -> R.drawable.ic_sun_cloud_angled_rain
                    "donner" -> R.drawable.ic_zaps
                    else -> R.drawable.ic_moon_cloud_fast_wind
                },
                contentScale = ContentScale.Fit,
                contentDesc = "Weather Icon",
                modifier = Modifier
                    .size(64.dp)
                    .padding(bottom = 4.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            )
            Text(
                text = weatherModelUpdate.time,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(4.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            )
            Text(
                text = "${weatherModelUpdate.temp}°",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.padding(4.dp).background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            )
        }
}

