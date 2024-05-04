package com.example.sharedcalendar.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.HexToJetpackColor
import com.example.sharedcalendar.R
import com.example.sharedcalendar.feature.editcalendar.EditCalendarScreen
import com.example.sharedcalendar.models.Calendar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.TimeZone

enum class ManageCalendarScreens(@StringRes val title: Int) {
    Select(title = R.string.select_calendar_screen), Edit(title = R.string.edit_calendar_screen), View(
        title = R.string.view_calendar_screen
    ),
    Create(title = R.string.create_calendar_screen),
}

class ManageCalendarsActivity : AppCompatActivity() {
    private val firebaseViewModel by viewModels<FirebaseViewModel>()
    private val viewModel by viewModels<ManageCalendarsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel.getUserShares()
        firebaseViewModel.userShares.observe(this) {
            firebaseViewModel.getCalendars()
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ManageCalendarScreen(firebaseViewModel, viewModel)
                }
            }

        }
    }
}

@Composable
fun DoseFAB(navController: NavController) {
    ExtendedFloatingActionButton(
        text = { Text(text = "Add Calendar") },
        icon = {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "add"
            )
        },
        onClick = {
            navController.navigate(ManageCalendarScreens.Create.name)
        },
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        containerColor = MaterialTheme.colorScheme.tertiary
    )
}

@Composable
fun ManageCalendarScreen(
    firebaseViewModel: FirebaseViewModel,
    viewModel: ManageCalendarsViewModel,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    val fabVisibility = rememberSaveable { (mutableStateOf(true)) }
    // Get the name of the current screen
    val currentScreen = ManageCalendarScreens.valueOf(
        backStackEntry?.destination?.route ?: ManageCalendarScreens.Select.name
    )
    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        floatingActionButton = {

            AnimatedVisibility(visible = fabVisibility.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                content = {
//                    DoseFAB(navController, analyticsHelper)
                })
        },
        topBar = {
            ManageCalendarsAppBar(currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() })
        }) { innerPadding ->
        val calendar by viewModel.calendar.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ManageCalendarScreens.Select.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = ManageCalendarScreens.Select.name) {
                SelectCalendarScreen(
                    modifier = Modifier.fillMaxSize(),
                    firebaseViewModel = firebaseViewModel,
                    onItemClicked = {
                        viewModel.setCalendar(it)
                        navController.navigate(ManageCalendarScreens.View.name)
                    },
                )
            }
            composable(route = ManageCalendarScreens.View.name) {
                val context = LocalContext.current
                ViewCalendarScreen(
                    modifier = Modifier.fillMaxSize(), calendar = calendar, onEditButtonClicked = {
                        viewModel.setCalendar(it)
                        navController.navigate(ManageCalendarScreens.Edit.name)
                    }, firebaseViewModel
                )
            }
            composable(route = ManageCalendarScreens.Edit.name) {
                EditCalendarScreen(modifier = Modifier.fillMaxSize(),
                    calendar = calendar,
                    firebaseViewModel,
                    onBackButtonClicked = {
                        navController.navigateUp()
                    })
            }
        }
    }
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCalendarsAppBar(
    currentScreen: ManageCalendarScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(stringResource(currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Button"
                    )
                }
            }
        })
}

@Composable
fun ProfileProperty(label: String, value: String) {
    Column {
        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp))
        Text(
            text = label,
//            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
//            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.bodyMedium, overflow = TextOverflow.Visible
        )
    }
}


@Composable
@Preview
fun ViewCalendarScreen(
    modifier: Modifier = Modifier,
    @PreviewParameter(CalendarPreviewParameterProvider::class) calendar: Calendar,
    onEditButtonClicked: (calendar: Calendar) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
) {
    val scrollState = rememberScrollState()
    Column(modifier = modifier.padding(16.dp)) {
        BoxWithConstraints {
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState),
                ) {

                    // Title
                    Column(
                        modifier = Modifier.padding(
                            start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp
                        )
                    ) {
                        Text(
                            text = calendar.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    val currentUser = Firebase.auth.currentUser
                    // Calendar Details
                    ProfileProperty(
                        "Timezone", calendar.timezone ?: TimeZone.getDefault().toString()
                    )
                    ProfileProperty(
                        "Owner", when (calendar.ownerId == currentUser?.uid) {
                            true -> currentUser?.displayName ?: "You own this calendar"
                            false -> calendar.owner?.name ?: firebaseViewModel.users.value?.get(
                                calendar.ownerId
                            )?.name ?: "Shared By Unknown User"
                        }
                    )
                    ProfileProperty("Access Scope", calendar.scope.toString())

                    Spacer(
                        Modifier.height(
                            (this@BoxWithConstraints.maxHeight - 320.dp).coerceAtLeast(
                                0.dp
                            )
                        )
                    )
                    if (calendar.scope == "Full" || calendar.scope == "Edit") {
                        Button(onClick = { onEditButtonClicked(calendar) }) {
                            Text(text = "Edit Calendar")
                        }
                    }
                }

            }
        }
    }
}


@Preview
@Composable
fun CalendarListItem(
    @PreviewParameter(CalendarPreviewParameterProvider::class) calendar: Calendar,
    onItemClicked: (calendar: Calendar) -> Unit = { ManageCalendarsViewModel().setCalendar(calendar) },
) {
    ListItem(
        headlineContent = {
            Text(
                text = calendar.name,
                modifier = Modifier.padding(horizontal = 16.dp),
//                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Icon(ImageVector.vectorResource(R.drawable.ic_baseline_circle_24),
                contentDescription = null,
                tint = calendar.color?.let { HexToJetpackColor.getColor(it) } ?: Color.Gray)
        },
        modifier = Modifier.clickable { onItemClicked(calendar) },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
    )
    HorizontalDivider()
}

@Composable
fun SelectCalendarList(
    calendars: List<Calendar>, onItemClicked: (calendar: Calendar) -> Unit, modifier: Modifier
) {
    LazyColumn(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = calendars.count(), key = { i -> calendars[i].id!! },
        ) { i ->
            CalendarListItem(calendars[i], onItemClicked)
        }
    }
}

@Composable
fun SelectCalendarScreen(
    modifier: Modifier = Modifier,
    firebaseViewModel: FirebaseViewModel = viewModel(),
    onItemClicked: (calendar: Calendar) -> Unit = { },
) {
    val calendars = firebaseViewModel.calendars.observeAsState()
    // API call
//    LaunchedEffect(key1 = Unit) {
//        firebaseViewModel.getCalendars()
////        firebaseViewModel.userShares.observe() {
////            firebaseViewModel.getCalendars()
////        }
//    }


    Log.i("Calendar List", calendars.toString())

    calendars.value?.toList()?.let {
        SelectCalendarList(
            calendars = it, onItemClicked = onItemClicked, modifier = modifier
        )
    }
}