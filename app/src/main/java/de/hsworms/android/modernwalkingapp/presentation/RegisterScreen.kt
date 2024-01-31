package de.hsworms.android.modernwalkingapp.presentation

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import de.hsworms.android.modernwalkingapp.domain.model.Profile
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation


@Composable
fun RegisterScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
        Content(navController, profileViewModel)
}


@SuppressLint("UnrememberedMutableState")
@Composable
private fun Content(navController: NavController, profileViewModel: ProfileViewModel) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val name = remember { mutableStateOf(TextFieldValue()) }
    val maleState = mutableStateOf(true)
    val femaleState = mutableStateOf(false)

    val heightState = remember { mutableStateOf(170) }
    val weightState: MutableState<Int> = remember { mutableStateOf(62) }
    val ageState: MutableState<Int> = remember { mutableStateOf(20) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            RoundedToggleButton(
                state = maleState,
                text = "Mänlich",
                onClick = {
                    maleState.value = true
                    femaleState.value = false
                },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
            )
            RoundedToggleButton(
                state = femaleState,
                text = "Weiblich",
                onClick = {
                    femaleState.value = true
                    maleState.value = false
                },
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
        }

        Row(modifier = Modifier.weight(3f)) {
            PickerView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                heightState = heightState,
                weightState = weightState,
                ageState = ageState
            )
        }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = "Name") },
                shape = RoundedCornerShape(16.dp),
                value = name.value,
                onValueChange = { name.value = it },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    //focusedBorderColor = Green,
                    unfocusedBorderColor = Gray)
            )

        Row(modifier = Modifier.weight(1f)) {
            RoundedButton(
                text = "Los geht's",
                onClick = {
                    //Toast.makeText(activity, "Save user Data in the db and navigate to the next screen", Toast.LENGTH_LONG).show()
                    createProfile(profileViewModel, name, ageState, heightState, weightState, maleState)
                    // save user Data in the db and navigate to the next screen
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
        }

    }

}

fun createProfile(profileViewModel: ProfileViewModel, name : MutableState<TextFieldValue>, age : MutableState<Int>, height : MutableState<Int>, weight : MutableState<Int>, gender : MutableState<Boolean>) {
    // female = 0 | male = 1
    val profileGender = if (gender.value) {
        1
    } else {
        0
    }

    val profile = Profile(name = name.value.text, age = age.value, height = height.value, weight = weight.value, gender = profileGender, xp = 230, bio = "", img = "")
    profileViewModel.createProfile(profile)

}


// components
@Composable
fun RoundedToggleButton(
    state: MutableState<Boolean>,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.White
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier then Modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (state.value) activeColor else inactiveColor,
        )
    ) {
        Text(text = text, fontSize = 18.sp, color = if (state.value) Color.White else Color.Black)
    }
}

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 4.dp,
    backGroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = modifier then Modifier.height(50.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backGroundColor,
            contentColor = contentColor
        ),
        elevation = ButtonDefaults.elevation(elevation)
    ) {
        Text(text = text, fontSize = 18.sp)
    }
}

@Composable
fun RoundedCard(
    modifier: Modifier = Modifier,
    color: Color = if (isSystemInDarkTheme()) Color.DarkGray else MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp,
    content: @Composable() () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        backgroundColor = color,
        elevation = elevation
    ) {
        content()
    }
}

@Composable
fun RoundIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color =  Color.Black.copy(alpha = 0.8f),
    backgroundColor: Color = if (isSystemInDarkTheme()) Color.White else MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp
) {
    Card(
        modifier = modifier
            .padding(all = 4.dp)
            .clickable(onClick = onClick)
            .then(Modifier.size(40.dp)),
        shape = CircleShape,
        backgroundColor = backgroundColor,
        elevation = elevation
    ) {
        Icon(imageVector, null, tint = tint)
    }
}


@Composable
private fun PickerView(
    modifier: Modifier = Modifier,
    heightState: MutableState<Int>,
    weightState: MutableState<Int>,
    ageState: MutableState<Int>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HeightSelector(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            heightState = heightState
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NumberPicker(
                label = "Gewicht",
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                pickerState = weightState
            )
            NumberPicker(
                label = "Alter",
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                pickerState = ageState
            )
        }
    }
}

@Composable
private fun HeightSelector(
    modifier: Modifier = Modifier,
    heightState: MutableState<Int>
) {
    val height = buildAnnotatedString {
        withStyle(
            style = SpanStyle(fontSize = 32.sp)
        ) { append(heightState.value.toString()) }
        append(" cm")
    }
    RoundedCard(modifier = modifier) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Größe",
                modifier = Modifier.padding(8.dp),
                style = TextStyle(fontSize = 32.sp)
            )
            Slider(
                value = heightState.value.toFloat(),
                onValueChange = { heightState.value = it.toInt() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                valueRange = (1f..272f),
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = height,
                modifier = Modifier.padding(8.dp),
                style = TextStyle(fontSize = 18.sp)
            )
        }
    }
}

@Composable
private fun NumberPicker(
    label: String,
    modifier: Modifier = Modifier,
    pickerState: MutableState<Int>,
    range: IntRange = 1..100
) {
    RoundedCard(modifier = modifier) {
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = TextStyle(fontSize = 18.sp),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = pickerState.value.toString(),
                style = TextStyle(fontSize = 32.sp),
                modifier = Modifier.padding(8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                RoundIconButton(imageVector = Icons.Default.Add, onClick = {
                    if (pickerState.value < range.last) {
                        pickerState.value = pickerState.value + 1
                    }
                })
                RoundIconButton(imageVector = Icons.Default.Remove, onClick = {
                    if (pickerState.value > range.first) {
                        pickerState.value = pickerState.value - 1
                    }
                })
            }
        }
    }
}