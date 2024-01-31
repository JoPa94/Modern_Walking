package de.hsworms.android.modernwalkingapp.domain.model

data class WeatherModel(
    val date: String = "21, Juli 2022",
    val description: String = "Cooling down with a chance of rain Sunday, Monday & Tuesday.",
    val condition: String = "Donner",
    val location: String = "Worms, Deutschland",
    val update: List<WeatherUpdate> = weatherModelList
) {
    data class WeatherUpdate(
        val time: String,
        val temp: String,
        val icon: String
    )
}


val weatherModelList = arrayListOf(
    WeatherModel.WeatherUpdate("00:00:00", "29.0", "wind"),
    WeatherModel.WeatherUpdate("02:30:00", "26.6", "rain"),
    WeatherModel.WeatherUpdate("04:00:00", "27.8", "thunder"),
    WeatherModel.WeatherUpdate("06:30:00", "29.3", "angledRain"),
    WeatherModel.WeatherUpdate("08:00:00", "25.5", "wind"),
    WeatherModel.WeatherUpdate("10:30:00", "26.8", "angledRain"),
)