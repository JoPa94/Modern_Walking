package de.hsworms.android.modernwalkingapp.presentation

import android.content.pm.ActivityInfo
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import de.hsworms.android.modernwalkingapp.R
import de.hsworms.android.modernwalkingapp.presentation.components.NavItem
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import de.hsworms.android.modernwalkingapp.domain.model.Profile
import de.hsworms.android.modernwalkingapp.ui.LockScreenOrientation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, profileViewModel: ProfileViewModel = viewModel()) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
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

    if (!profiles.isNullOrEmpty()) {
        profile = profiles[0]
    }

    val profileLevel = (profile.xp / 100) + 1
    val profileNextLevelReq = (profileLevel * 100)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Row(    // Card and Name
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(140.dp),

                ) {
                ShowProfileImage(profile)
            }
            Spacer(Modifier.width(50.dp))
            Text(
                text = stringResource(R.string.profil_hello_user) + " " + profile.name,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,

                )
        }
        Row(    //  Level and Progressbar
            modifier = Modifier
                .padding(top = 10.dp, bottom = 10.dp)

        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Spacer(Modifier.height(30.dp))
                Text(
                    text = "Level " + profileLevel,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
                Spacer(Modifier.height(10.dp))

                Text(
                    text = profile.xp.toString() + "/" + profileNextLevelReq.toString() + " XP",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressAnimated(profile)
                    Text(
                        text = (profile.xp % 100).toString() + "%",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                    )
                }

            }
        }
        Row(    //  Links
            modifier = Modifier
                .padding(top = 20.dp, bottom = 20.dp)

        ) {
            val listeVonLinks = listOf(
                Links(
                    title = "Profil bearbeiten",
                    route = "Edit"
                ),
                Links(
                    title = "Dark Mode",
                    route = NavItem.Profile.route
                ),
                Links(
                    title = "Sprache ändern",
                    route = NavItem.Profile.route
                ),
                Links(
                    title = "Über uns",
                    route = "Über uns"
                ),
            )
            LinkLazyColumn(navController, listeVonLinks)
        }

    }
}

@Composable
private fun CircularProgressAnimated(profile: Profile) {

    var progress by remember { mutableStateOf(0.0f) }
    progress = (profile.xp % 100).toFloat() / 100
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = if (progress >= 0.85f) {
            tween(  // kurz vor LevelUp slow Animation
                3000, 100, LinearOutSlowInEasing
            )
        } else {
            tween(1000, 100, FastOutSlowInEasing)
        }
    )
    CircularProgressIndicator(
        progress = animatedProgress,
        modifier = Modifier.size(150.dp),
        strokeWidth = 10.dp,

        )
}

@Composable
fun LinkLazyColumn(
    navController: NavController,
    listOfLinks: List<Links>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier) {
        listOfLinks.forEachIndexed { i, dataItem ->
            item(key = "header_$i") {
                Row(
                    Modifier
                        .padding(7.dp)
                        .clickable {
                            navController.navigate(dataItem.route)
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .weight(1f),
                    )
                    Icon(
                        Icons.Default.run {
                            //Icons.Filled.LocalFireDepartment
                            Icons.Filled.ChevronRight
                            //Icons.Filled.DirectionsRun
                        },
                        contentDescription = "",
                        tint = Color.LightGray,
                    )
                }
                Divider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 1.5.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowProfileImage(profile: Profile) {
    val imageUri = rememberSaveable { mutableStateOf(profile.img) }
    LaunchedEffect(Unit) {
        imageUri.value = profile.img
    }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.ic_user
        else {
            imageUri.value
        }
    )


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
                modifier = Modifier.size(150.dp),
                painter = painter,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Profilbild bearbeiten")
    }
}

data class Links(val title: String, val route: String)
