package com.example.sharedcalendar.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.models.Calendar


class ManageCalendarsActivity : AppCompatActivity() {
    private val firebaseViewModel by viewModels<FirebaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseViewModel.getUserShares()
        firebaseViewModel.userShares.observe(this) {
            firebaseViewModel.getCalendars()
        }

        setContent {
            MaterialTheme {
                CalendarList(firebaseViewModel)
            }

        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Preview
@Composable
fun CalendarListItem(@PreviewParameter(CalendarPreviewParameterProvider::class) calendar: Calendar) {
    ListItem(text = { Text(calendar.name) }, icon = {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier.size(56.dp)
        )
    }, modifier = Modifier.clickable { })
    Divider()
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun CalendarList(firebaseViewModel: FirebaseViewModel = viewModel()) {
    val calendars by firebaseViewModel.calendars.observeAsState()
    LazyColumn {
        calendars?.size?.let {
            items(it) {
                calendars?.forEach { calendar ->
                    CalendarListItem(calendar)
                }
            }
        }
    }
}