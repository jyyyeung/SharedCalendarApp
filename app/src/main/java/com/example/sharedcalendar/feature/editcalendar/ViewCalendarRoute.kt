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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
fun ViewCalendarScreen(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    onEditButtonClicked: (calendar: Calendar) -> Unit = {},
    firebaseViewModel: FirebaseViewModel = viewModel(),
    viewModel: ManageCalendarsViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val shares by firebaseViewModel.shares.observeAsState()
    val shouldShowDialog = remember { mutableStateOf(false) } // 1
    val rememberShare: MutableState<Share> = remember { mutableStateOf(Share()) }
    if (shouldShowDialog.value) {
        MyAlertDialog(shouldShowDialog = shouldShowDialog, rememberShare, firebaseViewModel)
    }

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
                    modifier = Modifier.wrapContentHeight(),
                    shouldShowDialog,
                    rememberShare,
                    calendar.scope ?: "View"
                )
            }

            Row(
                modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (calendar.scope == "Full" || calendar.scope == "edit") {
                    Button(
                        onClick = { onEditButtonClicked(calendar) },
                        modifier = Modifier.weight(1f),
                        enabled = (calendar.scope == "Full" || calendar.scope == "Edit")
                    ) {
                        Text(
                            text = "Edit Calendar"
                        )
                    }
                }

            }
        }
    }


}

@Composable
fun MyAlertDialog(
    shouldShowDialog: MutableState<Boolean>,
    share: MutableState<Share>,
    firebaseViewModel: FirebaseViewModel
) {
    if (shouldShowDialog.value) { // 2
        AlertDialog(
            // 3
            onDismissRequest = { // 4
                shouldShowDialog.value = false
            },
            // 5
            title = { Text(text = "Delete Share") },
            text = { Text(text = "Are you share you want to remove share with ${share.value.userEmail} of scope ${share.value.scope}?") },
            icon = { Icon(Icons.Default.Delete, contentDescription = "Delete") },
            confirmButton = { // 6
                TextButton(
                    onClick = {
                        shouldShowDialog.value = false
                        firebaseViewModel.deleteShare(share.value)
                    },

                    ) {
                    Text(
                        text = "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        shouldShowDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            },

            )
    }
}


@Composable
fun CalendarShareListItem(
    share: Share,
    shouldShowDialog: MutableState<Boolean>,
    rememberShare: MutableState<Share>,
    scope: String
) {
    ListItem(headlineContent = { Text(share.userEmail) },
        supportingContent = { share.scope?.let { Text(it) } },
        leadingContent = {
            if (scope == "Full") {

                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = "Account",
                )
            } else if (scope == "Edit") {
                Icon(Icons.Filled.Edit, contentDescription = "Edit")
            } else if (scope == "View") {
                Icon(Icons.Default.Info, contentDescription = "View")
            } else if (scope == "Availability") {
                Icon(Icons.Default.DateRange, contentDescription = "Availability")
            }
        },
        trailingContent = {
            if (scope == "Full" || scope == "Edit") {
                FilledTonalIconButton(onClick = {
                    shouldShowDialog.value = true
                    rememberShare.value = share
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    )
    HorizontalDivider()

}

@Composable
fun CalendarSharesList(
    shares: List<Share>,
    firebaseViewModel: FirebaseViewModel,
    modifier: Modifier = Modifier,
    shouldShowDialog: MutableState<Boolean>,
    rememberShare: MutableState<Share>,
    scope: String
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
            items(count = shares.count(), key = { i -> shares[i].id as Any }) { i ->
                CalendarShareListItem(share = shares[i], shouldShowDialog, rememberShare, scope)

            }
        }
    }
}