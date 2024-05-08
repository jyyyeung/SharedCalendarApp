package com.example.sharedcalendar.feature.editcalendar

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.models.Calendar
import com.example.sharedcalendar.models.Share
import com.example.sharedcalendar.ui.ManageCalendarsViewModel
import com.example.sharedcalendar.ui.ProfileProperty
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.TimeZone

@Composable
fun ViewCalendarRoute(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    onEditButtonClicked: (calendar: Calendar) -> Unit,
    firebaseViewModel: FirebaseViewModel,
    onBackButtonClicked: () -> Unit

) {
    ViewCalendarScreen(
        modifier, calendar, onEditButtonClicked, firebaseViewModel
    )
}

@Composable
fun ViewCalendarScreen(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    onEditButtonClicked: (calendar: Calendar) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    viewModel: ManageCalendarsViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val shares by firebaseViewModel.shares.observeAsState()
//    Column(modifier = modifier.padding(16.dp)) {
//        BoxWithConstraints {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp
                )
                .verticalScroll(scrollState),
        ) {
            // Title
            Column(
                modifier = Modifier
            ) {
                Text(
                    text = calendar.name,
                    style = MaterialTheme.typography.headlineLarge,
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
//
//                    Spacer(
//                        Modifier.height(
//                            (this@BoxWithConstraints.maxHeight - 320.dp).coerceAtLeast(
//                                0.dp
//                            )
//                        )
//                    )

            shares?.get(calendar.id.toString())?.toList()?.let {
                Log.i("View Calendar Route", shares.toString())
                CalendarSharesList(
                    shares = it,
                    firebaseViewModel = firebaseViewModel,
                    modifier = Modifier.wrapContentHeight()
                )
            }

//                    Spacer(
//                        Modifier.height(
//                            (this@BoxWithConstraints.maxHeight - 320.dp).coerceAtLeast(
//                                0.dp
//                            )
//                        )
//                    )

            Row(
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (calendar.scope != "Full" && calendar.scope != "Edit") {
                    Text(text = "You don't have the permissions to edit this calendar")
                }
                Button(
                    onClick = { onEditButtonClicked(calendar) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceBright
                    ),
                    enabled = (calendar.scope == "Full" || calendar.scope == "Edit")
                ) {
                    Text(
                        text = "Edit Calendar", color = MaterialTheme.colorScheme.onSurface
                    )
                }
//                Button(
//                    onClick = { /*TODO*/ },
//                    modifier = Modifier.weight(1f),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = MaterialTheme.colorScheme.surfaceBright
//                    )
//                ) {
//                    Text(
//                        text = "Delete Calendar", color = MaterialTheme.colorScheme.onSurface
//                    )
//                }

            }
        }
//            }
//        }
    }
}

@Composable
fun CalendarShareListItem(share: Share) {
    ListItem(headlineContent = { Text(share.userEmail) },
        supportingContent = { share.scope?.let { Text(it) } },
        leadingContent = {
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = "Account",
            )
        })
    HorizontalDivider()

}

@Composable
fun CalendarSharesList(
    shares: List<Share>, firebaseViewModel: FirebaseViewModel, modifier: Modifier = Modifier
) {


    if (shares.isNotEmpty()) {
        Text(
            text = "Calendar Shares",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(top = 8.dp)
        )

        LazyColumn(
            modifier = modifier.height(216.dp)
        ) {
            items(count = shares.count(), key = { i -> shares[i].id }) { i ->
                CalendarShareListItem(share = shares[i])

            }
        }
    }
}