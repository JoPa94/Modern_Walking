package de.hsworms.android.modernwalkingapp.presentation

import android.content.pm.ActivityInfo
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import de.hsworms.android.modernwalkingapp.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import de.hsworms.android.modernwalkingapp.domain.model.Profile
import de.hsworms.android.modernwalkingapp.presentation.components.NavItem
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation


@Composable
fun EditScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val notification = rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    if (notification.value.isNotEmpty()) {
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    val profiles = profileViewModel.profiles.observeAsState().value

    var profile = Profile(
        id = 0,
        name = "Test",
        age = 0,
        height = 0,
        weight = 0,
        gender = 0,
        xp = 0,
        bio = "",
        img = ""
    )

    var username by remember { mutableStateOf(profile.name) }
    var age by remember { mutableStateOf(profile.age.toString()) }
    var height by remember { mutableStateOf(profile.height.toString()) }
    var weight by remember { mutableStateOf(profile.weight.toString()) }
    var img by remember { mutableStateOf(profile.img) }
    if (!profiles.isNullOrEmpty()) {
        profile = profiles[0]
        LaunchedEffect(Unit) {
            username = profile.name
            age = profile.age.toString()
            height = profile.height.toString()
            weight = profile.weight.toString()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
    ) {

        img = profileImage(profile)
        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Nutzername",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = username,
                onValueChange = { it
                    if (it.isNotEmpty() && it.length < 20) {
                        username = it
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                ),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Alter",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = age,
                onValueChange = { it
                    if (it.isNotEmpty() && it.isDigitsOnly() && it.length < 4) {
                        age = it
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Größe",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = height,
                onValueChange = { it
                    if (it.isNotEmpty() && it.isDigitsOnly() && it.length < 4) {
                        height = it
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Gewicht",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(100.dp)
            )
            TextField(
                value = weight,
                onValueChange = { it
                    if (it.isNotEmpty() && it.isDigitsOnly() && it.length < 4) {
                        weight = it
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = MaterialTheme.colorScheme.primary,
                    containerColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                singleLine = true
                //ValueRange = ( 1..300 )
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(    // Save- Cancelbutton
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Button(onClick = {
                navController.popBackStack()
                Toast.makeText(context, "Änderungen verworfen", Toast.LENGTH_LONG).show()
            }) {
                Text(
                    text = "Abbruch",
                    fontSize = 15.sp
                )
            }
            Button(onClick = {
                profile.name = username
                profile.height = height.toInt()
                profile.weight = weight.toInt()
                profile.age = age.toInt()
                if(img.isNotEmpty()) {
                    profile.img = img
                }
                profileViewModel.updateProfile(profile)
                navController.navigate(NavItem.Profile.route)
                Toast.makeText(context, "Änderungen gespeichert", Toast.LENGTH_LONG).show()
            }) {
                Text(
                    text = "Speichern",
                    fontSize = 15.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun profileImage(profile: Profile): String {

    val imageUri = remember { mutableStateOf(profile.img) }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_user
        else
            imageUri.value
    )

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            uri?.let { imageUri.value = it.toString() }
        }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .padding(0.dp)
                .size(140.dp)
        ) {
            Image(
                contentScale = ContentScale.Crop,
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clickable { launcher.launch("image/*") },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Profilbild bearbeiten")

    }
    return imageUri.value
}
