package com.example.sharedcalendar.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.R
import com.example.sharedcalendar.models.Calendar

enum class ManageCalendarScreens(@StringRes val title: Int) {
    Select(title = R.string.select_calendar_screen), Edit(title = R.string.edit_calendar_screen), View(
        title = R.string.view_calendar_screen
    ),
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
                ManageCalendarScreen(firebaseViewModel, viewModel)
            }

        }
    }
}

@Composable
fun ManageCalendarScreen(
    firebaseViewModel: FirebaseViewModel,
    viewModel: ManageCalendarsViewModel,
    navController: NavHostController = rememberNavController()
) {
    // Get current back stack entry
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Get the name of the current screen
    val currentScreen = ManageCalendarScreens.valueOf(
        backStackEntry?.destination?.route ?: ManageCalendarScreens.Select.name
    )
    Scaffold(topBar = {
        ManageCalendarsAppBar(currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() })
    }) { _ ->
        val calendar by viewModel.calendar.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ManageCalendarScreens.Select.name,
//                        modifier = Modifier.padding(innerPadding)
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
                    calendar = calendar,
                )
            }
        }
    }
}

/**
 * Composable that displays the topBar and displays back button if back navigation is possible.
 */
@Composable
fun ManageCalendarsAppBar(
    currentScreen: ManageCalendarScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(title = { Text(stringResource(currentScreen.title)) },
//        colors = TopAppBarDefaults.mediumTopAppBarColors(
//            containerColor = MaterialTheme.colors.primarySurface
//        ),
        modifier = modifier, navigationIcon = {
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
fun ViewCalendarScreen(calendar: Calendar) {
    Text(text = calendar.name)
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CalendarListItem(
    @PreviewParameter(CalendarPreviewParameterProvider::class) calendar: Calendar,
    onItemClicked: (calendar: Calendar) -> Unit = { ManageCalendarsViewModel().setCalendar(calendar) },
) {
    ListItem(text = { Text(calendar.name) }, icon = {
        Icon(
            Icons.Filled.Favorite, contentDescription = null, modifier = Modifier.size(56.dp)
        )
    }, modifier = Modifier.clickable { onItemClicked(calendar) })
    Divider()
}

@Composable
fun SelectCalendarList(
    calendars: List<Calendar>, onItemClicked: (calendar: Calendar) -> Unit, modifier: Modifier
) {
    LazyColumn(modifier = modifier) {
        items(count = calendars.count(), key = { i -> calendars[i].id!! }) { i ->
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