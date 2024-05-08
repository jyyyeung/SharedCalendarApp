package com.example.sharedcalendar.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
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
import com.example.sharedcalendar.feature.editcalendar.ViewCalendarScreen
import com.example.sharedcalendar.models.Calendar

enum class ManageCalendarScreens(@StringRes val title: Int) {
    Select(title = R.string.select_calendar_screen),
    Edit(title = R.string.edit_calendar_screen), View(
        title = R.string.view_calendar_screen
    ),
    Create(title = R.string.create_calendar_screen),
}

class ManageCalendarsFragment : Fragment() {
    private lateinit var firebaseViewModel: FirebaseViewModel
    private val viewModel by viewModels<ManageCalendarsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel = ViewModelProvider(requireActivity())[FirebaseViewModel::class.java]
        firebaseViewModel.calendars.observe(requireActivity()) {
            firebaseViewModel.getShares(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        ManageCalendarScreen(
                            firebaseViewModel, viewModel
                        )

                    }
                }

            }
        }
//        return super.onCreateView(inflater, container, savedInstanceState)
    }

    companion object {
        private val TAG: String? = ManageCalendarsFragment::class.java.name
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
    navController: NavHostController = rememberNavController(),
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
            ManageCalendarsAppBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
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
                    }
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
    modifier: Modifier = Modifier,
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
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            modifier = Modifier.height(24.dp),
            style = MaterialTheme.typography.bodyMedium, overflow = TextOverflow.Visible
        )
    }
}


@Composable
fun CalendarListItem(
    calendar: Calendar,
    onItemClicked: (calendar: Calendar) -> Unit = {
        ManageCalendarsViewModel().setCalendar(
            calendar
        )
    },
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
//    val calendars = firebaseViewModel.calendars.observeAsState()
    // API call
//    LaunchedEffect(key1 = Unit) {
//        firebaseViewModel.getCalendars()
////        firebaseViewModel.userShares.observe() {
////            firebaseViewModel.getCalendars()
////        }
//    }
    val calendars = firebaseViewModel.getStaticCalendars()

    Log.i("Calendar List", calendars.toString())

    SelectCalendarList(
        calendars = calendars, onItemClicked = onItemClicked, modifier = modifier
    )
}